package com.james.renderEngine.uiElements;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.textRendering.dataTypes.Line;
import com.james.renderEngine.textRendering.TextMeshCreator;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.GuiGroup;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenSize;

import java.util.List;

/**
 * Creates a new text gui where each character in the text is a separate gui quad. Thus, with GuiText, one
 * VAO is shared among all characters, and each quad is simply scaled as needed. GuiText is well suited for
 * editable text fields, for text that uses updating color rules, and for text where characters are to be
 * additionally scaled independently of each other, among other applications. If it is not absolutely
 * necessary to use GuiText for a given use case, then it is recommended to use PersistentGuiText, as it is
 * a lot more efficient.
 *
 * @see PersistentGuiText
 */
public class GuiText extends AbstractGuiText implements GuiGroup {

    /**
     * @implNote A StringBuilder is created here to facilitate text editing should this GuiText be used for
     * editable text fields or similar applications. (This is exclusive to GuiText, as PersistentGuiText
     * doesn't support text editing.)
     */
    public GuiText(String text, FontInfo font, float fontSize, Position position) {
        super(text, font, fontSize, position);
        this.currentText = new StringBuilder(text);
    }

    // TODO: 2025-07-19 Maybe the StringBuilder should only be created once it is decided that the text should be editable?
    public GuiText(List<Line> lines, FontInfo font, float fontSize, Position position) {
        super(lines, font, fontSize, position);
        this.currentText = new StringBuilder(super.text);
    }

    @Override
    public void modifyGlobalColor(float r, float g, float b) {
        for (Gui textQuad : master.children) {
            textQuad.setSingleColor(r, g, b);
        }
    }

    @Override
    public void modifyGlobalScale(float globalScaleX, float globalScaleY) {
        for (Gui textQuad : master.children) {
            ScreenSize size = (ScreenSize) textQuad.size;
            size.setSize(globalScaleX * size.originalX, globalScaleY * size.originalY);
        }
    }

    /**
     * This is a method that is exclusive to GuiText, which allows for changing the individual character
     * colors for the text. To use it, a function must be passed in which outputs color based on inputs
     * of index and character. Note that this can only be called after apply() has already been called.
     */
    public void modifyColors(GuiTextFunction function) {
        char[] characters = super.text.replace(" ", "").toCharArray();

        for (int i = 0; i < characters.length; i++) {
            float[] color = function.getProperty(i, characters[i]);
            master.children.get(i).setSingleColor(color[0], color[1], color[2]);
        }
    }

    /**
     * This is a method that is exclusive to GuiText, which allows for changing the individual character
     * scales for the text. To use it, a function must be passed in which outputs an x scale and a y scale
     * based on inputs of index and character. Note that this can only be called after apply() has already
     * been called.
     */
    public void modifyScales(GuiTextFunction function) {
        char[] characters = super.text.replace(" ", "").toCharArray();

        for (int i = 0; i < characters.length; i++) {
            float[] scale = function.getProperty(i, characters[i]);
            ScreenSize size = (ScreenSize) master.children.get(i).size;
            size.setSize(scale[0] * size.originalX, scale[1] * size.originalY);
        }
    }

    @Override
    public void setVisibility(boolean visible) {
        for (Gui gui : master.children) {
            gui.isVisible = visible;
        }
    }

    /**
     * This is a method that is exclusive to GuiText. Updates the color information for every character of
     * the text, in case a ColorContents object has changed its progress value. This method should be called
     * from Screen subclasses, in the update() method.
     *
     * @implNote To update every character, calls applyUpdatedColorContentsForCharIndex() for each character
     * (excluding whitespaces).
     */
    public void applyUpdatedColorContentsForAllChars() {
        char[] charactersWithoutSpaces = text.replace(" ", "").toCharArray();

        for (int i = 0; i < charactersWithoutSpaces.length; i++) {
            applyUpdatedColorContentsForCharIndex(i, charactersWithoutSpaces);
        }
    }

    /**
     * This is a method that is exclusive to GuiText. Updates the color information for a single character
     * of the text, in case its ColorContents object has changed its progress value. This method should be
     * called from Screen subclasses, in the update() method. In addition, it is also called from the more
     * general applyUpdatedColorContentsForAllChars() method.
     *
     * Note that there is some similarity between this method and createGuis() found in TextMeshCreator.
     * @see TextMeshCreator
     *
     * @param index the position of the character in the text, excluding whitespaces
     * @param charactersWithoutSpaces if called from a Screen subclass, pass in null, and the method will
     *                                retrieve the charactersWithoutSpaces array itself; if the
     *                                charactersWithoutSpaces array is passed in manually, the method will
     *                                not need to retrieve it
     *
     * @implNote Passing in the charactersWithoutSpaces array manually is useful if the method is being
     * called from applyUpdatedColorContentsForAllChars(). This is so that the array won't need to be
     * continuously retrieved redundantly for every loop iteration of applyUpdatedColorContentsForAllChars().
     */
    public void applyUpdatedColorContentsForCharIndex(int index, char[] charactersWithoutSpaces) {
        if (charactersWithoutSpaces == null) {
            charactersWithoutSpaces = text.replace(" ", "").toCharArray();
        }

        float[] colors = textColorRules.getCharacterColor(index, charactersWithoutSpaces[index]);
        Gui guiForCharacter = master.children.get(index);

        if (colors.length == 3) {
            float r = colors[0];
            float g = colors[1];
            float b = colors[2];

            guiForCharacter.setSingleColor(r, g, b);
        }
        // if colors.length == 12, simply do nothing
        // since we can't modify color buffers at the moment

        guiForCharacter.apply();
    }

    private final StringBuilder currentText;
    private Screen screen;
    private int maxCharCount;

    public String getCurrentText() { return currentText.toString(); }

    /**
     * Allows the text for this GuiText to be edited at any time.
     *
     * @param screen the Screen that this GuiText belongs to; this is needed for adding and removing guis,
     *               which will happen frequently for editable text
     * @param maxCharCount the maximum number of characters; if 0 is passed in, there is no maximum
     */
    public void makeEditable(Screen screen, int maxCharCount) {
        if (maxCharCount < 0)
            throw new RuntimeException();

        this.screen = screen;
        this.maxCharCount = maxCharCount;
    }

    /**
     * After editing the currentText (such as if this text gui is to be used as a text field), this method
     * must be called in order for the text to change visually.
     */
    private void recreateTextQuads() {
        screen.removeGuis(master.children);
        apply();
        screen.addGuis(master.children);
    }

    /**
     * Adds a character to the text of this GuiText. makeEditable() must have been called first.
     */
    public void append(char character) {
        if (character == '\n' && maxLength == 0) {
            return;
        }

        if (maxCharCount == 0 || currentText.length() < maxCharCount) {
            currentText.append(character);
            text = currentText.toString();
            recreateTextQuads();
        }
    }

    /**
     * Removes the last character from the text of this GuiText (if there are still characters remaining).
     * makeEditable() must have been called first.
     */
    public void backspace() {
        if (currentText.length() > 0) {
            currentText.deleteCharAt(currentText.length() - 1);
            text = currentText.toString();
            recreateTextQuads();
        }
    }

    /**
     * Entirely overwrites the current text with some new text for this GuiText. makeEditable() must have been
     * called first.
     */
    public void setText(String newText) {
        currentText.replace(0, currentText.length(), newText);
        text = currentText.toString();
        recreateTextQuads();
    }

    /**
     * Whether to add a vertical bar after the text (this can be useful for text fields, where often a blinking
     * vertical bar lets users know that they can input text).
     */
    public void displayCarat(boolean displayCarat) {
        if (displayCarat) {
            text = currentText.toString() + "|";
        } else {
            text = currentText.toString();
        }

        recreateTextQuads();
    }

    private Gui master;

    @Override
    public void apply() {
        List<Line> finalLines;
        if (this.lines == null) finalLines = super.getLines();
        else finalLines = this.lines;

        this.master = TextMeshCreator.createGuis(position, singleColor, textColorRules, text, font, fontSize, finalLines, alignment, justified);
    }

    @Override
    public List<Gui> getAllGuis() {
        return master.children;
    }

    @FunctionalInterface
    public interface GuiTextFunction {
        float[] getProperty(int index, char character);
    }

}
