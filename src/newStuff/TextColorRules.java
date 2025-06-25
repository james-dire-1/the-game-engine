package newStuff;

import com.james.tools.RenderingMath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Set of rules used by GuiText or PersistentGuiText for text coloring. TextColorRules allows for flexible
 * and unique coloring of text which supports gradients and pure color blending. If all you want is single
 * color text with GuiText or PersistentGuiText, or if all you want is multi pure color text with GuiText,
 * then the use of TextColorRules is unnecessary. However, in all other text coloring cases, you must use
 * TextColorRules, as that is how GuiText and PersistentGuiText read in flexible color data.
 *
 * (Note that for the case of multi pure color text with GuiText mentioned above, the workaround for avoiding
 * TextColorRules is to use modifyColors() in GuiText. However, it is still recommended to use TextColorRules
 * for consistency.)
 *
 * TextColorRules supports both single color text rules and flexible rules (with CharacterColor). However,
 * the use of TextColorRules for the former case is overkill, as the only real benefit gained from using it
 * in that case is color blending. In fact, GuiText and PersistentGuiText don't expect color data to be in
 * TextColorRules if the data is merely a single color, so using TextColorRules then is at the programmer's
 * discretion. However, for any flexible rules (which must use CharacterColor), GuiText and PersistentGuiText
 * expect the color data to be in TextColorRules.
 *
 * @see com.james.renderEngine.uiElements.GuiText
 * @see PersistentGuiText
 * @see ColorContents
 */
public class TextColorRules {

    private List<float[]> singleColorList;
    private Supplier<ColorContents> singleColorFunction;

    private List<CharacterColor> characterColorList;
    private BiFunction<Integer, Character, ColorContents> characterColorFunction;

    /**
     * Initializes TextColorRules for use with single colors. Note that constructing TextColorRules in this
     * way is never necessary and is at the programmer's discretion.
     *
     * @param singleColorFunction the function for determining the ColorContents object at any given time
     */
    public TextColorRules(Supplier<ColorContents> singleColorFunction) {
        this.singleColorList = new ArrayList<>();
        this.singleColorFunction = singleColorFunction;
    }

    /**
     * Initializes TextColorRules for use with flexible color rules.
     *
     * @param characterColorFunction the function for determining the ColorContents object at any given time
     */
    public TextColorRules(BiFunction<Integer, Character, ColorContents> characterColorFunction) {
        this.characterColorList = new ArrayList<>();
        this.characterColorFunction = characterColorFunction;
    }

    /**
     * Adds a single color to the list of single colors that can be used by ColorContents for color blending
     * or as standalone colors. Note that the order in which single colors are added doubles up as the indices
     * used later by ColorContents objects.
     */
    public void addSingleColor(float[] singleColor) {
        singleColorList.add(singleColor);
    }

    /**
     * Adds a CharacterColor object to the list of CharacterColors that can be used by ColorContents for color
     * blending (if the two CharacterColors in question are each a pure color) or as standalone CharacterColors.
     * Note that the order in which CharacterColors are added doubles up as the indices used later by
     * ColorContents objects.
     */
    public void addCharacterColor(CharacterColor characterColor) {
        characterColorList.add(characterColor);
    }

    /**
     * Returns the final color (r, g, b) to be used for some text based on the ColorContents object
     * returned from the singleColorFunction, which may or may not blend two single colors. Not currently
     * used in engine code, so this method's use is at the programmer's discretion.
     */
    public float[] getSingleColor() {
        float[] finalColor;
        ColorContents contents = singleColorFunction.get();

        float[] startColor = singleColorList.get(contents.startColorIndex);

        if (!contents.blend) {
            finalColor = startColor;
        } else {
            float[] endColor = singleColorList.get(contents.endColorIndex);
            finalColor = blendColors(startColor, endColor, contents.progress);
        }

        return finalColor;
    }

    /**
     * Returns the final color (r, g, b) to be used for a single character in some text based on the
     * ColorContents object returned from the characterColorFunction, which may or may not blend two
     * CharacterColors. This method is used in TextMeshCreator while creating GuiTexts or PersistentGuiTexts,
     * and is used in GuiText while updating pure colors for GuiTexts.
     *
     * @implNote If the ColorContents in question uses a gradient-based CharacterColor for its start
     * color, then no color blending will occur at all, and instead the start color is returned immediately.
     *
     * @param index position of the character in the text excluding whitespaces, where the first character
     *              is index 0
     * @param character the actual character
     */
    public float[] getCharacterColor(int index, char character) {
        ColorContents contents = characterColorFunction.apply(index, character);

        CharacterColor startColor = characterColorList.get(contents.startColorIndex);
        if (startColor.usesGradient)
            return startColor.getAllColors();

        float[] finalColor;

        if (!contents.blend) {
            finalColor = startColor.getSingleColor();
        } else {
            CharacterColor endColor = characterColorList.get(contents.endColorIndex);
            if (endColor.usesGradient) throw new RuntimeException();

            finalColor = blendColors(startColor.getSingleColor(), endColor.getSingleColor(), contents.progress);
        }

        return finalColor;
    }

    /**
     * Blends two colors by a progress value to get a new color. Used for single color blending and for pure
     * color blending. At the moment, this method is not used for gradient blending (the engine currently
     * doesn't support gradient blending).
     */
    private static float[] blendColors(float[] startColor, float[] endColor, float progress) {
        float r = RenderingMath.linearlyInterpolate(startColor[0], endColor[0], progress);
        float g = RenderingMath.linearlyInterpolate(startColor[1], endColor[1], progress);
        float b = RenderingMath.linearlyInterpolate(startColor[2], endColor[2], progress);

        return new float[] {r, g, b};
    }

}
