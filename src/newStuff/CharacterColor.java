package newStuff;

public class CharacterColor {

    // TODO: 2025-01-01 Make note of the size of this array
    private final float[] colors;
    public final boolean usesGradient;

    private CharacterColor(float[] colors, boolean usesGradient) {
        this.colors = colors;
        this.usesGradient = usesGradient;
    }

    public float[] getVertexColor(int vertex) {
        float r = colors[3*vertex];
        float g = colors[3*vertex + 1];
        float b = colors[3*vertex + 2];

        return new float[] {r, g, b};
    }

    public float[] getSingleColor() {
        if (usesGradient) throw new RuntimeException();
        return colors;
    }

    public static CharacterColor setSingleColor(float r, float g, float b) {
        return new CharacterColor(new float[] {r, g, b}, false);
    }

    public static CharacterColor setDownUpColors(float[] down, float[] up) {
        if (down.length != 3 || up.length != 3) throw new RuntimeException();
        return new CharacterColor(new float[] {up[0], up[1], up[2], down[0], down[1], down[2], up[0], up[1], up[2], down[0], down[1], down[2]}, true);
    }

    public static CharacterColor setLeftRightColors(float[] left, float[] right) {
        if (left.length != 3 || right.length != 3) throw new RuntimeException();
        return new CharacterColor(new float[] {left[0], left[1], left[2], left[0], left[1], left[2], right[0], right[1], right[2], right[0], right[1], right[2]}, true);
    }

    public static CharacterColor setAllColors(float[] colors) {
        if (colors.length != 12) throw new RuntimeException();
        return new CharacterColor(colors, true);
    }

}
