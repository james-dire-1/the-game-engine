package com.james.renderEngine.visuals;

import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.shaders.interfaces.ILightShader;
import org.lwjgl.util.vector.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LightSettings {

    public static final int MAX_LIGHTS = 10;
    public static final Vector3f reusableVector = new Vector3f(0, 0, 0);

    private static float minBrightness = 0.2f;
    private static int userDefinedNumLightsInUse = 4;
    private static final Map<Integer, Light> idsToLightsMap = new HashMap<>();

    private static boolean newMinBrightness = false;
    private static boolean newNumLightsInUse = false;

    public static float getMinBrightness() { return minBrightness; }
    private static int getNumLightsInUse() { return Math.min(userDefinedNumLightsInUse, idsToLightsMap.size()); }
    public static Light getLight(int id) { return idsToLightsMap.get(id); }
    public static Collection<Light> getLights() { return idsToLightsMap.values(); }

    public static void setMinBrightness(float minBrightness) {
        if (LightSettings.minBrightness != minBrightness) {
            LightSettings.minBrightness = minBrightness;
            newMinBrightness = true;
        }
    }

    public static void setUserDefinedNumLightsInUse(int userDefinedNumLightsInUse) {
        if (userDefinedNumLightsInUse > MAX_LIGHTS)
            throw new RuntimeException();

        if (LightSettings.userDefinedNumLightsInUse != userDefinedNumLightsInUse) {
            LightSettings.userDefinedNumLightsInUse = userDefinedNumLightsInUse;
            newNumLightsInUse = true;
        }
    }

    public static void addLight(int id, Light light) {
        idsToLightsMap.put(id, light);
        if (idsToLightsMap.size() <= userDefinedNumLightsInUse) {
            newNumLightsInUse = true;
        }
    }

    public static boolean removeLight(int id) {
        Light removedLight = idsToLightsMap.remove(id);
        return removedLight != null;
    }

    public static void loadSettingsFirstTime(ILightShader shader) {
        shader.loadMinBrightness(minBrightness);
        shader.loadNumLightsInUse(getNumLightsInUse());
    }

    public static void loadFrameSettings(ILightShader shader) {
        if (newMinBrightness) shader.loadMinBrightness(minBrightness);
        if (newNumLightsInUse) shader.loadNumLightsInUse(getNumLightsInUse());

        if (idsToLightsMap.size() <= userDefinedNumLightsInUse) {
            shader.loadLights(idsToLightsMap.values().toArray(new Light[0]));
        }
    }

    public static void loadModelSettings(ILightShader shader, Model model) {
        shader.loadShineSettings(model.getShineSettings());
    }

    public static void loadGameObjectSettings(ILightShader shader, GameObject gameObject) {
        if (idsToLightsMap.size() > userDefinedNumLightsInUse) {
            shader.loadLights(LightSettings.getLights().toArray(new Light[0]));
        }
    }

    public static void resetState() {
        newMinBrightness = false;
        newNumLightsInUse = false;
    }

}
