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

    /**
     * Constructs a new Word. Requires font size for determining the length of Characters.
     */
    public Word(float fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Adds a Character to the Word and updates its current length.
     */
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
