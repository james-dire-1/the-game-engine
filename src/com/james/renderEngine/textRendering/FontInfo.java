package com.james.renderEngine.textRendering;

import com.james.renderEngine.textRendering.dataTypes.Character;
import templates.common.GlobalConstants;

import java.util.Map;

/**
 * Represents a font. Includes information for each character in the font as well as its texture atlas.
 */
public class FontInfo {

    public static final int SPACE_ASCII = 32;
    public static final int NEW_LINE_ASCII = 10;
    private static final int QUESTION_MARK_ASCII = 63;

    public final String textureAtlasPath;
    private final Map<Integer, Character> characters;

    public int lineHeight;

    public FontInfo(String fontFilePath, String textureAtlasPath) {
        if (!fontFilePath.endsWith(".fnt") || !textureAtlasPath.endsWith(".png")) {
            System.out.println("Incorrect file extensions for files. Verify that you are passing in correct files.");
            throw new RuntimeException();
        }

        String fullFontFilePath = GlobalConstants.FONTS_BASE_DIRECTORY + fontFilePath;
        String fullTextureAtlasPath = GlobalConstants.FONTS_BASE_DIRECTORY + textureAtlasPath;

        FontFileParser.FontFileContents contents = FontFileParser.parseFontFile(fullFontFilePath);
        this.characters = contents.characters;
        this.lineHeight = contents.lineHeight;
        this.textureAtlasPath = fullTextureAtlasPath;
    }

    /**
     * Gets the instance of the Character class for this font corresponding to the character passed in.
     */
    public Character getCharacterInfo(int characterInAscii) {
        return characters.getOrDefault(characterInAscii, characters.get(QUESTION_MARK_ASCII));
    }

}
