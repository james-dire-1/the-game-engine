package newStuff;

import com.james.tools.RenderingMath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class TextColorRules {

    private List<float[]> singleColorList;
    private Supplier<ColorContents> singleColorFunction;

    private List<CharacterColor> characterColorList;
    private BiFunction<Character, Integer, ColorContents> characterColorFunction;

    public TextColorRules(Supplier<ColorContents> singleColorFunction) {
        this.singleColorList = new ArrayList<>();
        this.singleColorFunction = singleColorFunction;
    }

    public TextColorRules(BiFunction<Character, Integer, ColorContents> characterColorFunction) {
        this.characterColorList = new ArrayList<>();
        this.characterColorFunction = characterColorFunction;
    }

    public void addSingleColor(float[] singleColor) {
        singleColorList.add(singleColor);
    }

    public void addCharacterColor(CharacterColor characterColor) {
        characterColorList.add(characterColor);
    }

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

    public float[] getCharacterColor(char character, int index) {
        float[] finalColor;
        ColorContents contents = characterColorFunction.apply(character, index);

        CharacterColor startColor = characterColorList.get(contents.startColorIndex);
        if (startColor.usesGradient) throw new RuntimeException();

        if (!contents.blend) {
            finalColor = startColor.getSingleColor();
        } else {
            CharacterColor endColor = characterColorList.get(contents.endColorIndex);
            if (endColor.usesGradient) throw new RuntimeException();

            finalColor = blendColors(startColor.getSingleColor(), endColor.getSingleColor(), contents.progress);
        }

        return finalColor;
    }

    private static float[] blendColors(float[] startColor, float[] endColor, float progress) {
        float r = RenderingMath.linearlyInterpolate(startColor[0], endColor[0], progress);
        float g = RenderingMath.linearlyInterpolate(startColor[1], endColor[1], progress);
        float b = RenderingMath.linearlyInterpolate(startColor[2], endColor[2], progress);

        return new float[] {r, g, b};
    }

    private static class ColorContents {
        private final boolean blend;
        private final int startColorIndex;
        private int endColorIndex;
        private float progress;

        public ColorContents(int startColorIndex, int endColorIndex, float progress) {
            this.blend = true;
            this.startColorIndex = startColorIndex;
            this.endColorIndex = endColorIndex;
            this.progress = progress;
        }

        public ColorContents(int color) {
            this.blend = false;
            this.startColorIndex = color;
        }
    }

}
