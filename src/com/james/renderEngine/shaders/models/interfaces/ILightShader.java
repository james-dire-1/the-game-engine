package com.james.renderEngine.shaders.models.interfaces;

import com.james.renderEngine.gameObjects.DirectionalLight;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.shaders.models.components.LightShaderFeature;
import com.james.renderEngine.visuals.ShineParameters;

public interface ILightShader {

    LightShaderFeature light();

    default void loadLights(Light[] lights) {
        light().loadLights(lights);
    }

    default void loadDirectionalLights(DirectionalLight[] directionalLights) {
        light().loadDirectionalLights(directionalLights);
    }

    default void loadShineParameters(ShineParameters shineParameters) {
        light().loadShineParameters(shineParameters);
    }

    default void loadMinBrightness(float minBrightness) {
        light().loadMinBrightness(minBrightness);
    }

    default void loadNumLightsInUse(int numLightsInUse) {
        light().loadNumLightsInUse(numLightsInUse);
    }

    default void loadNumDirectionalLightsInUse(int numDirectionalLightsInUse) {
        light().loadNumDirectionalLightsInUse(numDirectionalLightsInUse);
    }

}
