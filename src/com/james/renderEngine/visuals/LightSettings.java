package com.james.renderEngine.visuals;

import com.james.renderEngine.gameObjects.DirectionalLight;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.shaders.models.interfaces.ILightShader;
import com.james.tools.LightHandler;
import org.lwjgl.util.vector.Vector3f;

public class LightSettings {

    public static final int MAX_LIGHTS = 10;
    public static final int MAX_LIGHTS_DIRECTIONAL = 5;
    public static final Vector3f reusableVector = new Vector3f(0, 0, 0);

    private static float minBrightness = 0.2f;
    private static int userDefinedNumLightsInUse = 4;
    private static int userDefinedNumDirectionalLightsInUse = 2;

    private static boolean newMinBrightness = false;
    private static boolean newNumLightsInUse = false;
    private static boolean newNumDirectionalLightsInUse = false;

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

    public static void onDirectionalLightAdded() {
        if (lightHandler.numDirectionalLights() <= userDefinedNumDirectionalLightsInUse) {
            newNumDirectionalLightsInUse = true;
        }
    }

    public static void onLightRemoved() {
        if (lightHandler.numLights() < userDefinedNumLightsInUse) {
            newNumLightsInUse = true;
        }
    }

    public static void onDirectionalLightRemoved() {
        if (lightHandler.numDirectionalLights() < userDefinedNumDirectionalLightsInUse) {
            newNumDirectionalLightsInUse = true;
        }
    }

    private static int getNumLightsInUse() {
        return Math.min(userDefinedNumLightsInUse, lightHandler.numLights());
    }

    private static int getNumDirectionalLightsInUse() {
        return Math.min(userDefinedNumDirectionalLightsInUse, lightHandler.numDirectionalLights());
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

    public static void setUserDefinedNumDirectionalLightsInUse(int userDefinedNumDirectionalLightsInUse) {
        if (userDefinedNumDirectionalLightsInUse > MAX_LIGHTS_DIRECTIONAL)
            throw new RuntimeException();

        if (LightSettings.userDefinedNumDirectionalLightsInUse != userDefinedNumDirectionalLightsInUse) {
            LightSettings.userDefinedNumDirectionalLightsInUse = userDefinedNumDirectionalLightsInUse;
            newNumDirectionalLightsInUse = true;
        }
    }

    public static void loadSettingsFirstTime(ILightShader shader) {
        shader.loadMinBrightness(minBrightness);
    }

    public static void loadFrameSettings(ILightShader shader) {
        if (newMinBrightness) shader.loadMinBrightness(minBrightness);
        if (newNumLightsInUse) shader.loadNumLightsInUse(getNumLightsInUse());
        if (newNumDirectionalLightsInUse) shader.loadNumDirectionalLightsInUse(getNumDirectionalLightsInUse());

        if (lightHandler.numLights() <= userDefinedNumLightsInUse) {
            shader.loadLights(lightHandler.getLights().toArray(new Light[0]));
        }

        shader.loadDirectionalLights(lightHandler.getDirectionalLights().toArray(new DirectionalLight[0]));
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
        newNumDirectionalLightsInUse = false;
    }

}
