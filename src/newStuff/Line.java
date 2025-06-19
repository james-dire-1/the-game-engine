package newStuff;

import com.james.renderEngine.textRendering.Character;
import com.james.renderEngine.textRendering.FontInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of text to be rendered on screen. Used for multi line text.
 */
public class Line {

    public final List<Integer> asciiCodes = new ArrayList<>();
    public float currentLength;

    private float whitespaceLength;
    private float maxLength;

    public Line(Character whitespace, float fontSize, float maxLength) {
        this.whitespaceLength = whitespace.xAdvance * fontSize;
        this.maxLength = maxLength;
    }

    public Line(byte[] asciiCodesToAdd, FontInfo font, float fontSize) {
        this.currentLength = TextOrganizer.getTotalWidthOfSingleLineText(font, fontSize, asciiCodesToAdd);

        for (int asciiCode : asciiCodesToAdd) {
            if (asciiCode == FontInfo.NEW_LINE_ASCII)
                throw new RuntimeException();

            this.asciiCodes.add(asciiCode);
        }
    }

    /**
     * If there is room on the line, the word will be added.
     * @return whether the word was successfully added
     * @implNote If this the first word being added to the line, then the check for space on the line is
     * skipped. This is to prevent a case where a very long word that is longer than the max length will
     * repeatedly attempt to occupy successive lines.
     */
    public boolean attemptToAddWord(Word word) {
        boolean success;

        if (currentLength + word.currentLength <= maxLength || asciiCodes.isEmpty()) {
            success = true;
            asciiCodes.addAll(word.asciiCodes);
            currentLength += word.currentLength;
        } else {
            success = false;
        }

        return success;
    }

    /**
     * If there is room on the line, the whitespace will be added.
     * @return whether the whitespace was successfully added
     */
    public boolean attemptToAddWhitespace() {
        boolean success;

        if (currentLength + whitespaceLength <= maxLength) {
            success = true;
            asciiCodes.add(FontInfo.SPACE_ASCII);
            currentLength += whitespaceLength;
        } else {
            success = false;
        }

        return success;
    }

    // TODO: 2024-12-30 There is something interesting here which should be considered
    public void trimSingleTrailingWhitespace() {
        int lastIndex = asciiCodes.size()-1;
        if (lastIndex == -1) return;

        int lastAsciiCode = asciiCodes.get(lastIndex);
        if (lastAsciiCode == FontInfo.SPACE_ASCII) {
            asciiCodes.remove(lastIndex);
            currentLength -= whitespaceLength;
        }
    }

    public float getMaxLength() {
        return maxLength;
    }

}
