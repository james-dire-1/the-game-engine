package newStuff;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.dataTypes.Position;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

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

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    public void setSingleColor(float r, float g, float b) {
        this.singleColor[0] = r;
        this.singleColor[1] = g;
        this.singleColor[2] = b;
    }

    public void setTextColorRules(TextColorRules textColorRules) {
        this.textColorRules = textColorRules;
    }

    public abstract void modifyGlobalColor(float r, float g, float b);

    public abstract void modifyGlobalScale(float globalScaleX, float globalScaleY);

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

    protected abstract void apply();

}
