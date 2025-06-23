package newStuff;

import com.james.renderEngine.textRendering.Character;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.texturing.TextureBank;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.utilities.VertexUtilityArrays;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextMeshCreator {

    private static final float maxJustifiedSpaceLength = 35;

    /**
     * @implNote When constructing the parent/master gui, the size doesn't matter, since it will not get
     * rendered on the screen and its children (the text quads) will not need to make use of the parent gui's
     * size, since they don't use normalized size. Rather, they use screen size. However, the constructor still
     * expects them, so a random one is supplied.
     */



    public static Gui createGuis(Position position, float[] singleColor, TextColorRules textColorRules,
                                 String originalText, FontInfo font, float fontSize, List<Line> lines,
                                 TextAlignment alignment, boolean justified) {
        Gui master = new Gui(position, new ScreenSize(0, 0), null);
        float cursorY = 0;

        List<Gui> textQuads = new ArrayList<>();

        for (Line line : lines) {
            boolean lastLine = lines.indexOf(line) == lines.size()-1;

            float cursorX = TextOrganizer.getStartCursorPosition(alignment, line, justified, lastLine);
            List<Integer> asciiCodes = line.asciiCodes;

            float spaceLengthJustified = 0;
            if (justified) {
                if (lastLine) {
                    spaceLengthJustified = font.getCharacterInfo(FontInfo.SPACE_ASCII).xAdvance*fontSize;
                } else {
                    spaceLengthJustified = TextOrganizer.getWhitespaceLengthForJustifiedText(font, fontSize, line);
                }
            }

            for (int asciiCode : asciiCodes) {
                Character character = font.getCharacterInfo(asciiCode);

                if (character.id != FontInfo.SPACE_ASCII) {
                    Gui textQuad = addTextQuad(cursorX, cursorY, font, fontSize, character, master);
                    textQuads.add(textQuad);
                }

                if (character.id == FontInfo.SPACE_ASCII && justified) {
                    cursorX += spaceLengthJustified;
                } else {
                    cursorX += character.xAdvance * fontSize;
                }
            }

            cursorY -= font.lineHeight*fontSize;
        }

        // Setting the colors for the text and final applying

        if (textColorRules == null) {
            for (Gui guiForCharacter : textQuads) {
                guiForCharacter.setSingleColor(singleColor[0], singleColor[1], singleColor[2]);
                guiForCharacter.apply();
            }
        } else {
            char[] charactersWithoutSpaces = originalText.replace(" ", "").toCharArray();

            // TODO: 2025-06-23 remove these print outs
            System.out.println("charactersWithoutSpaces.length = " + charactersWithoutSpaces.length);
            System.out.println("textQuads.size() = " + textQuads.size());

            for (int i = 0; i < charactersWithoutSpaces.length; i++) {
                float[] colors = textColorRules.getCharacterColor(i, charactersWithoutSpaces[i]);
                Gui guiForCharacter = textQuads.get(i);

                if (colors.length == 3) {
                    float r = colors[0];
                    float g = colors[1];
                    float b = colors[2];

                    guiForCharacter.setSingleColor(r, g, b);
                } else if (colors.length == 12) {

                    guiForCharacter.setColors(colors);

                } else {
                    throw new RuntimeException();
                }

                guiForCharacter.apply();
            }
        }

        return master;
    }

    /**
     * Adds a text quad to the list of guis. CONTINUE HERE LATER
     *
     * @implNote Note that the y offset must be negated whereas the x offset must be kept as is. This is just the
     * way font files are done.
     */
    private static Gui addTextQuad(float cursorX, float cursorY, FontInfo font, float fontSize, 
                                   Character character, Gui parent) {
        ScreenPosition positionForCharacter = new ScreenPosition(Math.round(cursorX + character.xOffset*fontSize), Math.round(cursorY - character.yOffset*fontSize));
        ScreenSize sizeForCharacter = new ScreenSize(Math.round(character.width*fontSize), Math.round(character.height*fontSize));

        Gui guiForCharacter = new Gui(positionForCharacter, sizeForCharacter, parent, VertexUtilityArrays.textQuadVertexPositions, VertexUtilityArrays.defaultIndices);

        guiForCharacter.setTextureAndSamplingData(font.textureAtlasPath, character.x, character.y, character.width, character.height, false);
        guiForCharacter.markAsText();

        return guiForCharacter;
    }

    // TODO: 2024-12-27 color parameter will somehow have to be changed in the future
    public static Gui createMesh(Position position, float[] singleColor, TextColorRules textColorRules,
                                 String originalText, FontInfo font, float fontSize, List<Line> lines,
                                 TextAlignment alignment, boolean justified) {
        List<Float> vertexPositions = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();

        BufferedImage image = TextureBank.getTexture(font.textureAtlasPath);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        float cursorY = 0;
        int currentCharacterIndex = 0;

        for (Line line : lines) {
            boolean lastLine = lines.indexOf(line) == lines.size()-1;

            float cursorX = TextOrganizer.getStartCursorPosition(alignment, line, justified, lastLine);
            List<Integer> asciiCodes = line.asciiCodes;

            float spaceLengthJustified = 0;
            if (justified) {
                if (lastLine) {
                    spaceLengthJustified = font.getCharacterInfo(FontInfo.SPACE_ASCII).xAdvance*fontSize;
                } else {
                    spaceLengthJustified = TextOrganizer.getWhitespaceLengthForJustifiedText(font, fontSize, line);
                }
            }

            for (int asciiCode : asciiCodes) {
                Character character = font.getCharacterInfo(asciiCode);

                if (character.id != FontInfo.SPACE_ASCII) {
                    addCharacterToMesh(cursorX, cursorY, imageWidth, imageHeight, fontSize, character,
                            currentCharacterIndex, vertexPositions, indices, textureCoords);

                    currentCharacterIndex++;
                }

                if (character.id == FontInfo.SPACE_ASCII && justified) {
                    cursorX += spaceLengthJustified;
                } else {
                    cursorX += character.xAdvance * fontSize;
                }
            }

            cursorY -= font.lineHeight*fontSize;
        }

        return createMeshInstance(position, singleColor, textColorRules, originalText, font.textureAtlasPath, vertexPositions, indices, textureCoords);
    }

    private static Gui createMeshInstance(Position position, float[] singleColor, TextColorRules textColorRules,
                                          String originalText, String textureAtlasPath, List<Float> vertexPositions,
                                          List<Integer> indices, List<Float> textureCoords) {
        // Making raw arrays out of their respective object types

        Float[] vertexPositionsArray = vertexPositions.toArray(new Float[0]);
        Integer[] indicesArray = indices.toArray(new Integer[0]);
        Float[] textureCoordsArray = textureCoords.toArray(new Float[0]);

        float[] finalVertexPositionsArray = new float[vertexPositionsArray.length];
        for (int i = 0; i < finalVertexPositionsArray.length; i++) {
            finalVertexPositionsArray[i] = vertexPositionsArray[i];
        }

        int[] finalIndicesArray = new int[indicesArray.length];
        for (int i = 0; i < finalIndicesArray.length; i++) {
            finalIndicesArray[i] = indicesArray[i];
        }

        float[] finalTextureCoordsArray = new float[textureCoordsArray.length];
        for (int i = 0; i < finalTextureCoordsArray.length; i++) {
            finalTextureCoordsArray[i] = textureCoordsArray[i];
        }

        Gui mesh = new Gui(position, new ScreenSize(1, 1), null, finalVertexPositionsArray, finalIndicesArray);
        mesh.setTextureAndSamplingData(textureAtlasPath, finalTextureCoordsArray);
        mesh.markAsText();

        // Setting the colors for the text

        if (textColorRules == null) {
            mesh.setSingleColor(singleColor[0], singleColor[1], singleColor[2]);
        } else {
            char[] charactersWithoutSpaces = originalText.replace(" ", "").toCharArray();
            float[] finalColorsArray = new float[charactersWithoutSpaces.length * 4 * 3];

            for (int i = 0; i < charactersWithoutSpaces.length; i++) {
                float[] colors = textColorRules.getCharacterColor(i, charactersWithoutSpaces[i]);

                if (colors.length == 3) {
                    float r = colors[0];
                    float g = colors[1];
                    float b = colors[2];

                    for (int corner = 0; corner < 4; corner++) {
                        finalColorsArray[i * 4 * 3 + corner * 3] = r;
                        finalColorsArray[i * 4 * 3 + corner * 3 + 1] = g;
                        finalColorsArray[i * 4 * 3 + corner * 3 + 2] = b;
                    }
                } else if (colors.length == 12) {
                    for (int corner = 0; corner < 4; corner++) {
                        float r = colors[corner * 3];
                        float g = colors[corner * 3 + 1];
                        float b = colors[corner * 3 + 2];

                        finalColorsArray[i * 4 * 3 + corner * 3] = r;
                        finalColorsArray[i * 4 * 3 + corner * 3 + 1] = g;
                        finalColorsArray[i * 4 * 3 + corner * 3 + 2] = b;
                    }
                } else {
                    throw new RuntimeException();
                }
            }

             mesh.setColors(finalColorsArray);
        }

        // Final applying

        mesh.apply();

        return mesh;
    }

    private static void addCharacterToMesh(float cursorX, float cursorY, int imageWidth, int imageHeight,
                                           float fontSize, Character character, int currentCharacterIndex,
                                           List<Float> vertexPositions, List<Integer> indices,
                                           List<Float> textureCoords) {
        float xLeft = cursorX + character.xOffset*fontSize;
        float yTop = cursorY - character.yOffset*fontSize;
        float xRight = xLeft + character.width*fontSize;
        float yBottom = yTop - character.height*fontSize;

        float xTexLeft = (float)character.x / imageWidth;
        float yTexTop = (float)character.y / imageHeight;
        float xTexRight = xTexLeft + (float)character.width / imageWidth;
        float yTexBottom = yTexTop + (float)character.height / imageHeight;

        Float[] vertexPositionsToAdd = {
                xLeft, yTop, 0f,
                xLeft, yBottom, 0f,
                xRight, yTop, 0f,
                xRight, yBottom, 0f
        };

        Integer[] indicesToAdd = {
                4*currentCharacterIndex,     4*currentCharacterIndex + 1, 4*currentCharacterIndex + 2,
                4*currentCharacterIndex + 2, 4*currentCharacterIndex + 1, 4*currentCharacterIndex + 3
        };

        Float[] textureCoordsToAdd = {
                xTexLeft, yTexTop,
                xTexLeft, yTexBottom,
                xTexRight, yTexTop,
                xTexRight, yTexBottom
        };

        vertexPositions.addAll(Arrays.asList(vertexPositionsToAdd));
        indices.addAll(Arrays.asList(indicesToAdd));
        textureCoords.addAll(Arrays.asList(textureCoordsToAdd));
    }

}
