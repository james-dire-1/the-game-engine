package com.james.renderEngine.textRendering.coloring;

/**
 * A class that allows for blending between two CharacterColors, provided that both CharacterColors represent
 * pure colors (are not gradients). Instances of this class serve as outputs to the functions that are passed
 * into TextColorRules instances.
 *
 * @see TextColorRules
 */
public class ColorContents {

    public final boolean blend;
    public final int startColorIndex;
    public int endColorIndex;
    public float progress;

    /**
     * Constructs a ColorContents object where blending should occur between two CharacterColors.
     *
     * @param startColorIndex the index/id of one CharacterColor instance; this index number is determined by
     *                        the order in which CharacterColor instances are added to a given TextColorRules
     *                        instance
     * @param endColorIndex the index/id of the other CharacterColor instance
     * @param progress determines how the two CharacterColors should be blended, where 0 will result in only
     *                 the color given by startColorIndex, and 1 will result in only the color given by
     *                 endColorIndex
     */
    public ColorContents(int startColorIndex, int endColorIndex, float progress) {
        this.blend = true;
        this.startColorIndex = startColorIndex;
        this.endColorIndex = endColorIndex;
        this.progress = progress;
    }

    /**
     * Constructs a ColorContents object where no blending occurs. That is, only one CharacterColor is used.
     * In this case, only startColorIndex is initialized.
     */
    public ColorContents(int colorIndex) {
        this.blend = false;
        this.startColorIndex = colorIndex;
    }

}
