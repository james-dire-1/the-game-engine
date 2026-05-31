package com.james.renderEngine.visuals;

import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.shaders.models.interfaces.ILightShader;
import com.james.tools.LightHandler;
import org.lwjgl.util.vector.Vector3f;

public class LightSettings {

    public static final int MAX_LIGHTS = 10;
    public static final Vector3f reusableVector = new Vector3f(0, 0, 0);

    private static float minBrightness = 0.2f;
    private static int userDefinedNumLightsInUse = 4;

    private static boolean newMinBrightness = false;
    private static boolean newNumLightsInUse = false;

    private static LightHandler lightHandler;
    private static final ClosestKLightsSelector selector =
            new ClosestKLightsSelector(userDefinedNumLightsInUse);

    public static void setLightHandler(LightHandler lightHandler) {
        LightSettings.lightHandler = lightHandler;
    }

    public static void onLightAdded() {
        if (lightHandler.numLights() <= userDefinedNumLightsInUse) {
            newNumLightsInUse = true;
        }
    }

    public static void onLightRemoved() {
        if (lightHandler.numLights() < userDefinedNumLightsInUse) {
            newNumLightsInUse = true;
        }
    }

    private static int getNumLightsInUse() {
        return Math.min(userDefinedNumLightsInUse, lightHandler.numLights());
    }

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

    public static void loadSettingsFirstTime(ILightShader shader) {
        shader.loadMinBrightness(minBrightness);
    }

    public static void loadFrameSettings(ILightShader shader) {
        if (newMinBrightness) shader.loadMinBrightness(minBrightness);
        if (newNumLightsInUse) shader.loadNumLightsInUse(getNumLightsInUse());

        if (lightHandler.numLights() <= userDefinedNumLightsInUse) {
            shader.loadLights(lightHandler.getLights().toArray(new Light[0]));
        }
    }

    public static void loadModelSettings(ILightShader shader, Model model) {
        shader.loadShineSettings(model.getShineSettings());
    }

    public static void loadGameObjectSettings(ILightShader shader, GameObject gameObject) {
        if (lightHandler.numLights() > userDefinedNumLightsInUse) {
            selector.setK(userDefinedNumLightsInUse);
            Light[] allLights = lightHandler.getLights().toArray(new Light[0]);
            selector.determineClosest(allLights, gameObject.getPosition());
            shader.loadLights(selector.getClosestLights());
        }
    }

    public static void resetState() {
        newMinBrightness = false;
        newNumLightsInUse = false;
    }

}
