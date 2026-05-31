package com.james.renderEngine.shaders.models.components;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.texturing.ShineSettings;
import com.james.renderEngine.visuals.LightSettings;
import org.lwjgl.util.vector.Vector3f;

public class LightShaderFeature implements ShaderFeature {

    private final Shader shader;

    private int location_reflectivity;
    private int location_shineDamper;
    private int location_minBrightness;
    private int location_numLightsInUse;
    private int[] location_lightPositions;
    private int[] location_lightColors;

    public LightShaderFeature(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void getUniformLocations() {
        location_reflectivity = shader.getUniformLocation("reflectivity");
        location_shineDamper = shader.getUniformLocation("shineDamper");
        location_minBrightness = shader.getUniformLocation("minBrightness");
        location_numLightsInUse = shader.getUniformLocation("numLightsInUse");

        location_lightPositions = new int[LightSettings.MAX_LIGHTS];
        location_lightColors = new int[LightSettings.MAX_LIGHTS];

        for (int i = 0; i < LightSettings.MAX_LIGHTS; i++) {
            String lightPositionName = String.format("lightPositions[%d]", i);
            String lightColorName = String.format("lightColors[%d]", i);
            location_lightPositions[i] = shader.getUniformLocation(lightPositionName);
            location_lightColors[i] = shader.getUniformLocation(lightColorName);
        }
    }

    public void loadLights(Light[] lights) {
        for (int i = 0; i < LightSettings.MAX_LIGHTS; i++) {
            Vector3f positionToLoad;
            Vector3f colorToLoad;

            if (i < lights.length) {
                Light light = lights[i];
                positionToLoad = light.getPosition();
                colorToLoad = light.getColor();
            } else {
                positionToLoad = LightSettings.reusableVector;
                colorToLoad = LightSettings.reusableVector;
            }

            shader.loadVector3fToUniform(location_lightPositions[i], positionToLoad);
            shader.loadVector3fToUniform(location_lightColors[i], colorToLoad);
        }
    }

    public void loadShineSettings(ShineSettings shineSettings) {
        float reflectivity = 0;
        float shineDamper = 0;

        if (shineSettings != null) {
            reflectivity = shineSettings.reflectivity;
            shineDamper = shineSettings.shineDamper;
        }

        shader.loadFloatToUniform(location_reflectivity, reflectivity);
        shader.loadFloatToUniform(location_shineDamper, shineDamper);
    }

    public void loadMinBrightness(float minBrightness) {
        shader.loadFloatToUniform(location_minBrightness, minBrightness);
    }

    public void loadNumLightsInUse(int numLightsInUse) {
        if (numLightsInUse > LightSettings.MAX_LIGHTS)
            throw new RuntimeException();

        shader.loadIntegerToUniform(location_numLightsInUse, numLightsInUse);
    }

}
