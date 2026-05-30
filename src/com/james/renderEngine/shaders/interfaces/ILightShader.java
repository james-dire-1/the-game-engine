package com.james.renderEngine.shaders.interfaces;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.texturing.ShineSettings;

public interface ILightShader {

    void loadLights(Light[] light);
    void loadShineSettings(ShineSettings shineSettings);
    void loadMinBrightness(float minBrightness);
    void loadNumLightsInUse(int numLightsInUse);

}
