package newStuff;

import com.james.renderEngine.textRendering.Character;
import com.james.renderEngine.textRendering.FontInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for parsing font (.fnt) files. Also specifies some padding constants that are applied on top
 * of the information given in font files.
 */
public class FontFileParser {

    public static final int DESIRED_PADDING = 3;

    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;

    /**
     * Reads in a font file and returns a map of ascii codes to their respective Character instances, as well
     * as the line height suggested by the font file. Padding corrections are done too.
     *
     * @see Character
     */
    public static FontFileContents parseFontFile(String fontFile) {
        Map<Integer, Character> characters = new HashMap<>();
        int lineHeight = 0;

        try {

            InputStream stream = Objects.requireNonNull(FontInfo.class.getResourceAsStream(fontFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;

            int[] paddingValues = null;
            int paddingWidth = 0;
            int paddingHeight = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("info ")) {
                    String[] generalSettings = line.split(" ");
                    for (String setting : generalSettings) {
                        if (setting.startsWith("padding=")) {
                            String[] paddingValuesAsStrings = setting.split("padding=")[1].split(",");

                            paddingValues = new int[paddingValuesAsStrings.length];
                            for (int i = 0; i < paddingValues.length; i++) {
                                paddingValues[i] = Integer.parseInt(paddingValuesAsStrings[i]);
                            }

                            paddingWidth = paddingValues[PAD_LEFT] + paddingValues[PAD_RIGHT];
                            paddingHeight = paddingValues[PAD_TOP] + paddingValues[PAD_BOTTOM];
                        }
                    }
                }

                if (line.startsWith("common ")) {
                    String[] generalSettings = line.split(" ");
                    for (String setting : generalSettings) {
                        if (setting.startsWith("lineHeight=")) {
                            String lineHeightAsString = setting.split("lineHeight=")[1];
                            lineHeight = Integer.parseInt(lineHeightAsString);
                        }
                    }
                }

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

                // corrections that take padding values into account
                x += (paddingValues[PAD_LEFT] - DESIRED_PADDING);
                y += (paddingValues[PAD_TOP] - DESIRED_PADDING);
                width -= (paddingWidth - (2 * DESIRED_PADDING));
                height -= (paddingHeight - (2 * DESIRED_PADDING));
                xoffset += (paddingValues[PAD_LEFT] - DESIRED_PADDING);
                yoffset += (paddingValues[PAD_TOP] - DESIRED_PADDING);
                xadvance -= paddingWidth;

                characters.put(id, new Character(id, x, y, width, height, xoffset, yoffset, xadvance));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FontFileContents(characters, lineHeight);
    }

    public static class FontFileContents {
        public final Map<Integer, Character> characters;
        public final int lineHeight;

        public FontFileContents(Map<Integer, Character> characters, int lineHeight) {
            this.characters = characters;
            this.lineHeight = lineHeight;
        }
    }

}
