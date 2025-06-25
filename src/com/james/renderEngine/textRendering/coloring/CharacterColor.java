package com.james.renderEngine.textRendering.coloring;

import com.james.renderEngine.uiElements.PersistentGuiText;

/**
 * Class to customize the color appearance of characters in text that belongs to either a GuiText or to a
 * PersistentGuiText. A CharacterColor can represent either a pure color or a color gradient (where each of the
 * four corners of the text quad can be a separate color).
 *
 * @implNote Note that here, the order of colors specified in a gradient is as follows: top-left, bottom-left,
 * top-right, then bottom-right.
 *
 * @see com.james.renderEngine.uiElements.GuiText
 * @see PersistentGuiText
 */
public class CharacterColor {

    private final float[] colors;
    public final boolean usesGradient;

    /**
     * Constructor for initializing the colors array for this CharacterColor, and for setting whether the
     * CharacterColor uses a gradient or not. If the length of the colors array is 3, then a pure color is being
     * used. If the length of the colors array is 12, then a gradient is being used. The constructor is private
     * since one of the static utility functions below should be used.
     */
    private CharacterColor(float[] colors, boolean usesGradient) {
        this.colors = colors;
        this.usesGradient = usesGradient;
    }

    /**
     * Gets the four corner colors defined for this CharacterColor's gradient (assumes the CharacterColor is
     * using a gradient).
     */
    public float[] getAllColors() {
        if (!usesGradient) throw new RuntimeException();
        return colors;
    }

    /**
     * Gets the pure color defined for this CharacterColor (assumes the CharacterColor is using a pure color).
     */
    public float[] getSingleColor() {
        if (usesGradient) throw new RuntimeException();
        return colors;
    }

    /**
     * Constructs and returns a CharacterColor that uses a single color.
     */
    public static CharacterColor setSingleColor(float r, float g, float b) {
        return new CharacterColor(new float[] {r, g, b}, false);
    }

    /**
     * Constructs and returns a CharacterColor that uses a vertical gradient.
     */
    public static CharacterColor setDownUpColors(float[] down, float[] up) {
        if (down.length != 3 || up.length != 3) throw new RuntimeException();
        return new CharacterColor(new float[] {up[0], up[1], up[2], down[0], down[1], down[2], up[0], up[1], up[2], down[0], down[1], down[2]}, true);
    }

    /**
     * Constructs and returns a CharacterColor that uses a horizontal gradient.
     */
    public static CharacterColor setLeftRightColors(float[] left, float[] right) {
        if (left.length != 3 || right.length != 3) throw new RuntimeException();
        return new CharacterColor(new float[] {left[0], left[1], left[2], left[0], left[1], left[2], right[0], right[1], right[2], right[0], right[1], right[2]}, true);
    }

    /**
     * Constructs and returns a CharacterColor that uses a four-way gradient (one color for each corner of the
     * text quad).
     */
    public static CharacterColor setAllColors(float[] colors) {
        if (colors.length != 12) throw new RuntimeException();
        return new CharacterColor(colors, true);
    }

}
