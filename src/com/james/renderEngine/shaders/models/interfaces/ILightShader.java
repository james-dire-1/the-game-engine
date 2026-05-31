package com.james.renderEngine.shaders.models.interfaces;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.shaders.models.components.LightShaderFeature;
import com.james.renderEngine.texturing.ShineSettings;

public interface ILightShader {

    LightShaderFeature light();

    default void loadLights(Light[] lights) {
        light().loadLights(lights);
    }

    default void loadShineSettings(ShineSettings shineSettings) {
        light().loadShineSettings(shineSettings);
    }

    default void loadMinBrightness(float minBrightness) {
        light().loadMinBrightness(minBrightness);
    }

    default void loadNumLightsInUse(int numLightsInUse) {
        light().loadNumLightsInUse(numLightsInUse);
    }

}
