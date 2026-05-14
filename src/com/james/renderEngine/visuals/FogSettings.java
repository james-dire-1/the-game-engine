package com.james.renderEngine.visuals;

import com.james.tools.ColorUtils;
import com.james.renderEngine.rendering.interfaces.IFogShader;
import org.lwjgl.util.vector.Vector3f;

public class FogSettings {

    // public static final float DEFAULT_DENSITY = 0.007f;
    // public static final float DEFAULT_GRADIENT = 1.5f;
    // private static final Vector3f skyColor = ColorUtils.asNormalizedRGBVector(0x34eba8)[0];

    public static final float DEFAULT_DENSITY = 0.0025f;
    public static final float DEFAULT_GRADIENT = 5f;

    private static final Vector3f skyColor = ColorUtils.asNormalizedRGBVector(0x6CC2F0)[0];
    private static boolean useSphericalFog = true;
    private static float fogDensity = DEFAULT_DENSITY;
    private static float fogGradient = DEFAULT_GRADIENT;

    private static boolean newSkyColor = false;
    private static boolean newUseSphericalFog = false;
    private static boolean newFogDensity = false;
    private static boolean newFogGradient = false;

    public static Vector3f getSkyColor() { return skyColor; }
    public static boolean getUseSphericalFog() { return useSphericalFog; }
    public static float getFogDensity() { return fogDensity; }
    public static float getFogGradient() { return fogGradient; }

    public static void setSkyColor(float r, float g, float b) {
        if (skyColor.x != r || skyColor.y != g || skyColor.z != b) {
            skyColor.set(r, g, b);
            newSkyColor = true;
        }
    }

    public static void setUseSphericalFog(boolean useSphericalFog) {
        if (FogSettings.useSphericalFog != useSphericalFog) {
            FogSettings.useSphericalFog = useSphericalFog;
            newUseSphericalFog = true;
        }
    }

    public static void setFogDensity(float fogDensity) {
        if (FogSettings.fogDensity != fogDensity) {
            FogSettings.fogDensity = fogDensity;
            newFogDensity = true;
        }
    }

    public static void setFogGradient(float fogGradient) {
        if (FogSettings.fogGradient != fogGradient) {
            FogSettings.fogGradient = fogGradient;
            newFogGradient = true;
        }
    }

    public static void loadSettingsFirstTime(IFogShader shader) {
        shader.loadSkyColor(skyColor);
        shader.loadFogType(useSphericalFog);
        shader.loadFogDensity(fogDensity);
        shader.loadFogGradient(fogGradient);
    }

    public static void loadSettings(IFogShader shader) {
        if (newSkyColor) shader.loadSkyColor(skyColor);
        if (newUseSphericalFog) shader.loadFogType(useSphericalFog);
        if (newFogDensity) shader.loadFogDensity(fogDensity);
        if (newFogGradient) shader.loadFogGradient(fogGradient);
    }

    public static void resetState() {
        newSkyColor = false;
        newUseSphericalFog = false;
        newFogDensity = false;
        newFogGradient = false;
    }

}
