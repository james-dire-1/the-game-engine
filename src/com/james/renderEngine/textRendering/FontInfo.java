package com.james.renderEngine.textRendering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a font. Includes information for each character in the font as well as its texture atlas.
 */
public class FontInfo {

    public final String textureAtlasPath;
    private final Map<Integer, Character> characters;

    public FontInfo(String fontFilePath, String textureAtlasPath) {
        if (!fontFilePath.endsWith(".fnt") || !textureAtlasPath.endsWith(".png"))
            throw new IllegalStateException("Incorrect file extensions for files. Verify that you are passing in correct files.");

        this.characters = parseFontFile(fontFilePath);
        this.textureAtlasPath = textureAtlasPath;
    }

    /**
     * Reads in a font file and returns a map of Ascii codes to their respective Character instances
     */
    private static Map<Integer, Character> parseFontFile(String fontFile) {
        Map<Integer, Character> characters = new HashMap<>();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(FontInfo.class.getResourceAsStream(fontFile)));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("char ")) continue;
                String[] settingsForCharacter = line.split(" ");

                int id = 0;
                int x = 0;
                int y = 0;
                int width = 0;
                int height = 0;
                int xoffset = 0;
                int yoffset = 0;
                int xadvance = 0;

                for (String setting : settingsForCharacter) {
                    if (setting.equals("")) continue;

                    if (setting.startsWith("id=")) {
                        id = Integer.parseInt(setting.split("id=")[1]);
                    } else if (setting.startsWith("x=")) {
                        x = Integer.parseInt(setting.split("x=")[1]);
                    } else if (setting.startsWith("y=")) {
                        y = Integer.parseInt(setting.split("y=")[1]);
                    } else if (setting.startsWith("width=")) {
                        width = Integer.parseInt(setting.split("width=")[1]);
                    } else if (setting.startsWith("height=")) {
                        height = Integer.parseInt(setting.split("height=")[1]);
                    } else if (setting.startsWith("xoffset=")) {
                        xoffset = Integer.parseInt(setting.split("xoffset=")[1]);
                    } else if (setting.startsWith("yoffset=")) {
                        yoffset = Integer.parseInt(setting.split("yoffset=")[1]);
                    } else if (setting.startsWith("xadvance=")) {
                        xadvance = Integer.parseInt(setting.split("xadvance=")[1]);
                    }
                }

                characters.put(id, new Character(x, y, width, height, xoffset, yoffset, xadvance));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return characters;
    }

    /**
     * Gets the instance of the Character class for this font corresponding to the character passed in.
     *
     * @implNote Gets the Ascii code of the character by casting it to an int, then retrieves the instance
     * of the Character class from the map using this code as the key
     */
    public Character getCharacterInfo(int characterInAscii) {
        return characters.get(characterInAscii);
    }

}
