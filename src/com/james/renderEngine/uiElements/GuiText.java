package com.james.renderEngine.uiElements;

import com.james.renderEngine.textRendering.Character;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.utilities.VertexUtilityArrays;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Creates a new text gui.
 */
public class GuiText {

    public final Gui master;

    /**
     * Creates a GuiText. Since a GuiText is just a bunch of quad guis for each character, it is asked
     * that you pass the list of guis used for rendering as one of the parameters for this constructor,
     * so that all the gui quads can be added to the list.
     *
     * @implNote If the text is LEFT_ALIGNED, then currentCursorPosition will just be 0. No point
     * writing an if clause for that.
     * When constructing the parent gui, the size doesn't matter, since it will not get rendered
     * on the screen and its children (the text quads) will not need to make use of the parent gui's size,
     * since they don't use normalized size. Rather, they use screen size. However, the constructor still
     * expects them, so a random one is supplied.
     */
    // TODO: 2024-06-03 the fact that we're still using Guis for invisible frames is not a good idea
    // TODO: 2024-06-03 Perhaps reconsider the structure of the code to allow for a different way to go about this?
    public GuiText(String text, FontInfo font, float fontSize, TextAlignment alignment, List<Gui> guis, Position position) {
        byte[] asciiCodes = text.getBytes(StandardCharsets.US_ASCII);

        float currentCursorPosition = 0;
        if (alignment == TextAlignment.CENTER_ALIGNED) {
            currentCursorPosition = getStartCursorPositionForCenteredText(font, fontSize, asciiCodes);
        }

        this.master = new Gui(position, new ScreenSize(0, 0), null);
        for (byte asciiCode : asciiCodes) {
            Character character = font.getCharacterInfo(asciiCode);

            currentCursorPosition += addTextQuadAndAdvanceCursor(font, fontSize, character, guis, master, currentCursorPosition);
        }
    }

    /**
     * Gets the starting cursor position for text that needs to be centered.
     *
     * @implNote First gets the total width of the text by looping through all the characters and adding on
     * their x advance (or width for the last character). The total width is then halved and negated to get
     * the starting cursor position, which should be negative for centered text.
     */
    private static float getStartCursorPositionForCenteredText(FontInfo font, float fontSize, byte[] asciiCodes) {
        float totalWidthOfText = 0;

        for (int i = 0; i < asciiCodes.length; i++) {
            Character character = font.getCharacterInfo(asciiCodes[i]);

            if (i == asciiCodes.length-1) { // last element of the array
                totalWidthOfText += character.width*fontSize;
            } else { // other elements of the array
                totalWidthOfText += character.xAdvance*fontSize;
            }
        }

        return -totalWidthOfText/2;
    }

    /**
     * Adds a text quad to the list of guis and advances the position of the virtual cursor to get ready for
     * the next text quad.
     *
     * @implNote Note that the y offset must be negated whereas the x offset must be kept as is. This is just the
     * way font files are done.
     *
     * @return the new cursor position after adding the text quad to the list
     */
    private static float addTextQuadAndAdvanceCursor(FontInfo font, float fontSize, Character character, List<Gui> guis, Gui parent, float currentCursorPosition) {
        ScreenPosition positionForCharacter = new ScreenPosition((int)(currentCursorPosition + character.xOffset*fontSize), (int)(-character.yOffset*fontSize));
        ScreenSize sizeForCharacter = new ScreenSize((int)(character.width*fontSize), (int)(character.height*fontSize));

        Gui guiForCharacter = new Gui(positionForCharacter, sizeForCharacter, parent, VertexUtilityArrays.textQuadVertexPositions, VertexUtilityArrays.defaultIndices);
        guiForCharacter.setTextureAndSamplingData(font.textureAtlasPath, character.x, character.y, character.width, character.height, false);
        guiForCharacter.apply();
        guis.add(guiForCharacter);

        return character.xAdvance*fontSize;
    }

    public enum TextAlignment {
        LEFT_ALIGNED,
        CENTER_ALIGNED
    }

}
