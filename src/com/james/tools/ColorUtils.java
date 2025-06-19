package com.james.tools;

public class ColorUtils {

    /**
     * @implNote In this implementation, each int in the parameter refers to either red, green, or blue component.
     * The value of each int ranges from 0 to 255.
     */
    public static float[] asNormalizedRGB(int[] color) {
        float normalizedR = (float)color[0] / 255;
        float normalizedG = (float)color[1] / 255;
        float normalizedB = (float)color[2] / 255;

        return new float[] {normalizedR, normalizedG, normalizedB};
    }

    /**
     * @implNote In this implementation, each int in the parameter refers to a whole RGB color, which will be
     * extracted. The value of each int ranges from (-2^31) to (+2^31 - 1)
     */
    public static float[] asNormalizedRGBArray(int... hexRGBValues) {
        float[] normalizedRGBArray = new float[hexRGBValues.length * 3];

        for (int i = 0; i < hexRGBValues.length; i++) {
            int hexRGB = hexRGBValues[i];

            int r = (hexRGB & 0x00ff0000) >> 16;
            int g = (hexRGB & 0x0000ff00) >> 8;
            int b = (hexRGB & 0x000000ff);

            normalizedRGBArray[i * 3] = (float)r / 255;
            normalizedRGBArray[i * 3 + 1] = (float)g / 255;
            normalizedRGBArray[i * 3 + 2] = (float)b / 255;
        }

        return normalizedRGBArray;
    }

    // https://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe
    public static float[] HSVtoRGB(float hue, float saturation, float value) {
        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        float[] rgbArray = null;

        switch (h) {
            case 0: rgbArray = new float[] {value, t, p}; break;
            case 1: rgbArray = new float[] {q, value, p}; break;
            case 2: rgbArray = new float[] {p, value, t}; break;
            case 3: rgbArray = new float[] {p, q, value}; break;
            case 4: rgbArray = new float[] {t, p, value}; break;
            case 5: rgbArray = new float[] {value, p, q}; break;
            default: rgbArray = new float[] {1, 1, 1}; break;
        }

        return rgbArray;
    }

}
