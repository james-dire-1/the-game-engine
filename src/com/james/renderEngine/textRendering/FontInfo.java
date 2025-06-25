package com.james.renderEngine.textRendering;

import com.james.renderEngine.textRendering.dataTypes.Character;

import java.util.Map;

/**
 * Represents a font. Includes information for each character in the font as well as its texture atlas.
 */
public class FontInfo {

    public static final int SPACE_ASCII = 32;
    public static final int NEW_LINE_ASCII = 10;

    public final String textureAtlasPath;
    private final Map<Integer, Character> characters;

    public int lineHeight;

    public FontInfo(String fontFilePath, String textureAtlasPath) {
        if (!fontFilePath.endsWith(".fnt") || !textureAtlasPath.endsWith(".png"))
            throw new IllegalStateException("Incorrect file extensions for files. Verify that you are passing in correct files.");

        FontFileParser.FontFileContents contents = FontFileParser.parseFontFile(fontFilePath);
        this.characters = contents.characters;
        this.lineHeight = contents.lineHeight;
        this.textureAtlasPath = textureAtlasPath;
    }

    /**
     * Gets the instance of the Character class for this font corresponding to the character passed in.
     */
    public Character getCharacterInfo(int characterInAscii) {
        return characters.get(characterInAscii);
    }

}
