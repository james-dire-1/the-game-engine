package newStuff;

import com.james.renderEngine.textRendering.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a word to be rendered on screen. Used for multi line text.
 */
public class Word {

    public final List<Integer> asciiCodes = new ArrayList<>();
    public float currentLength;

    private final float fontSize;

    public Word(float fontSize) {
        this.fontSize = fontSize;
    }

    public void addCharacter(Character character) {
        asciiCodes.add(character.id);
        currentLength += character.xAdvance * fontSize;
    }

    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        for (int asciiCode : asciiCodes) {
            word.append((char)asciiCode);
        }

        return word.toString();
    }
}
