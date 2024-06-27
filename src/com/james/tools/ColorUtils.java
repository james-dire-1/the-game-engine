package com.james.tools;

public class ColorUtils {

    public static float[] asNormalizedRGB(int[] color) {
        float normalizedR = (float)color[0] / 255;
        float normalizedG = (float)color[1] / 255;
        float normalizedB = (float)color[2] / 255;

        return new float[] {normalizedR, normalizedG, normalizedB};
    }

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

}
