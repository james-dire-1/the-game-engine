package com.james.renderEngine.textRendering;

import com.james.renderEngine.textRendering.dataTypes.Character;
import com.james.renderEngine.textRendering.dataTypes.Line;
import com.james.renderEngine.textRendering.dataTypes.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Various utility functions for organizing text.
 */
public class TextOrganizer {

    /**
     * Organizes a string of text to be arranged in individual lines. Necessary for multi line text.
     * @param maxLength the maximum length of a line
     * @param asciiCodes the string of text
     *
     * @return a list of Line objects
     */
    public static List<Line> organizeMultiLineText(FontInfo fontInfo, float fontSize, float maxLength, byte[] asciiCodes) {
        Character whitespace = fontInfo.getCharacterInfo(FontInfo.SPACE_ASCII);
        List<Line> allLines = new ArrayList<>();

        Line currentLine = new Line(whitespace, fontSize, maxLength);
        allLines.add(currentLine);
        Word currentWord = new Word(fontSize);

        for (byte asciiCode : asciiCodes) {
            Character character = fontInfo.getCharacterInfo(asciiCode);

            if (asciiCode == FontInfo.SPACE_ASCII || asciiCode == FontInfo.NEW_LINE_ASCII) {
                if (!currentWord.asciiCodes.isEmpty()) {
                    boolean successAppendingWord = currentLine.attemptToAddWord(currentWord);
                    if (!successAppendingWord) {
                        currentLine = new Line(whitespace, fontSize, maxLength);
                        allLines.add(currentLine);
                        currentLine.attemptToAddWord(currentWord);

                        if (asciiCode == FontInfo.NEW_LINE_ASCII) {
                            currentLine = new Line(whitespace, fontSize, maxLength);
                            allLines.add(currentLine);
                        }
                    } else if (asciiCode == FontInfo.NEW_LINE_ASCII) {
                        currentLine = new Line(whitespace, fontSize, maxLength);
                        allLines.add(currentLine);
                    }

                    currentWord = new Word(fontSize);
                } else if (asciiCode == FontInfo.NEW_LINE_ASCII) {
                    currentLine = new Line(whitespace, fontSize, maxLength);
                    allLines.add(currentLine);
                }

                if (asciiCode == FontInfo.SPACE_ASCII) {
                    boolean successAppendingSpace = currentLine.attemptToAddWhitespace();
                    if (!successAppendingSpace) {
                        currentLine = new Line(whitespace, fontSize, maxLength);
                        allLines.add(currentLine);
                    }
                }
            } else {
                currentWord.addCharacter(character);
            }

        }

        boolean successAppendingWord = currentLine.attemptToAddWord(currentWord);
        if (!successAppendingWord) {
            currentLine = new Line(whitespace, fontSize, maxLength);
            allLines.add(currentLine);
            currentLine.attemptToAddWord(currentWord);
        }

        for (Line line : allLines) {
            line.trimSingleTrailingWhitespace();
        }

        return allLines;
    }

    /**
     * Gets the starting cursor position for both single line and multi line text.
     */
    public static float getStartCursorPosition(TextAlignment alignment, Line line, boolean justified, boolean lastLine) {
        float length = (justified && !lastLine) ? line.getMaxLength() : line.currentLength;

        float position = 0;
        if (alignment == TextAlignment.CENTER_ALIGNED) {
            position = -length / 2;
        } else if (alignment == TextAlignment.RIGHT_ALIGNED) {
            position = -length;
        }

        return position;
    }

    /**
     * Gets the total width of single line text.
     */
    public static float getTotalWidthOfSingleLineText(FontInfo font, float fontSize, byte[] asciiCodes) {
        float totalWidthOfText = 0;

        for (int asciiCode : asciiCodes) {
            Character character = font.getCharacterInfo(asciiCode);
            totalWidthOfText += character.xAdvance * fontSize;
        }

        return totalWidthOfText;
    }

    /**
     * Gets the horizontal length that a whitespace should be if text is justified.
     */
    public static float getWhitespaceLengthForJustifiedText(FontInfo font, float fontSize, Line line) {
        int numberOfWhitespaces = 0;
        float lengthWithoutWhitespaces = line.currentLength;

        for (Integer asciiCode : line.asciiCodes) {
            if (asciiCode == FontInfo.SPACE_ASCII) {
                numberOfWhitespaces++;
                lengthWithoutWhitespaces -= font.getCharacterInfo(asciiCode).xAdvance*fontSize;
            }
        }

        return (line.getMaxLength() - lengthWithoutWhitespaces) / numberOfWhitespaces;
    }

}
