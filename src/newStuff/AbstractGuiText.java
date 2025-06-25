package newStuff;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.dataTypes.Position;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * General gui text class for both GuiText and PersistentGuiText. Contains methods and fields that are common
 * to both GuiText and PersistentGuiText.
 *
 * @implNote Many of the fields in this class serve the purpose of saving information about the text that needs
 * to be given to the apply() function later. Thus, modifying many of these fields after apply() has already
 * been called has no effect on the rendered text.
 */
public abstract class AbstractGuiText {

    public String text;
    protected final FontInfo font;
    protected final float fontSize;
    protected final Position position;

    protected final float[] singleColor = { 0, 0, 0 };
    protected TextColorRules textColorRules;

    protected TextAlignment alignment = TextAlignment.LEFT_ALIGNED;
    protected int maxLength;
    protected boolean justified = false;

    public AbstractGuiText(String text, FontInfo font, float fontSize, Position position) {
        this.text = text;
        this.font = font;
        this.fontSize = fontSize;
        this.position = position;
    }

    /**
     * Aligns text. This has an effect on where text will be rendered relative to the value given by the
     * position field.
     */
    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    /**
     * Changes the color used for all characters in the text. Note that this should be called before apply()
     * has been called. Calling it after will result in no visual change.
     */
    public void setSingleColor(float r, float g, float b) {
        this.singleColor[0] = r;
        this.singleColor[1] = g;
        this.singleColor[2] = b;
    }

    /**
     * Adds TextColorRules for the text gui to use, allowing for complex text coloring that extends beyond
     * basic single colors, such as gradients and individual character coloring. When a TextColorRules object
     * is added to a text gui, the text will adopt those rules and will ignore the singleColor field.
     */
    public void setTextColorRules(TextColorRules textColorRules) {
        this.textColorRules = textColorRules;
    }

    /**
     * Method to modify global color, the color shared by all characters in this text. To be used only if the
     * text is already using a single color (so it's not using TextColorRules). Note that this can only be
     * called after apply() has already been called.
     */
    public abstract void modifyGlobalColor(float r, float g, float b);

    /**
     * Method to modify global scale. This has an appearance that depends on whether it is called on a GuiText
     * or a PersistentGuiText. If called on PersistentGuiText, the text as a whole is scaled (like changing
     * the font size), whereas if called on GuiText, each individual character is scaled (so characters can
     * begin overlapping if scaled too much). Note that this can only be called after apply() has already
     * been called.
     */
    public abstract void modifyGlobalScale(float globalScaleX, float globalScaleY);

    /**
     * Sets the text gui to use a maximum line length, so that any text that exceeds this line length will
     * be placed on the next line.
     */
    public void wrapAndSetMaxLength(int maxLength) {
        if (maxLength <= 0)
            throw new RuntimeException();

        this.maxLength = maxLength;
    }

    public void justify() {
        this.justified = true;
    }

    protected List<Line> getLines() {
        byte[] asciiCodes = text.getBytes(StandardCharsets.US_ASCII);
        List<Line> lines;

        if (maxLength != 0) {
            lines = TextOrganizer.organizeMultiLineText(font, fontSize, maxLength, asciiCodes);
        } else {
            lines = Collections.singletonList(new Line(asciiCodes, font, fontSize));
        }

        return lines;
    }

    /**
     * Once all the properties of the text gui have been finalized, apply() must be called to construct
     * the necessary Gui objects to represent the text. Implementation is dependent on whether the text is
     * using GuiText or PersistentGuiText.
     */
    protected abstract void apply();

}
