package com.james.renderEngine.shaders.models;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.shaders.interfaces.ILightShader;
import com.james.renderEngine.texturing.ShineSettings;
import com.james.renderEngine.shaders.interfaces.IFogShader;
import com.james.renderEngine.visuals.LightSettings;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TexturedModelShader extends Shader implements ILightShader, IFogShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/texturedModelVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/texturedModelFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_reflectivity;
    private int location_shineDamper;
    private int location_minBrightness;
    private int location_numLightsInUse;
    private int location_skyColor;
    private int location_fogType;
    private int location_fogDensity;
    private int location_fogGradient;
    private int location_fogApplied;
    private int location_cameraPosition;

    private int[] location_lightPositions;
    private int[] location_lightColors;

    public TexturedModelShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_minBrightness = super.getUniformLocation("minBrightness");
        location_numLightsInUse = super.getUniformLocation("numLightsInUse");
        location_skyColor = super.getUniformLocation("skyColor");
        location_fogType = super.getUniformLocation("fogType");
        location_fogDensity = super.getUniformLocation("fogDensity");
        location_fogGradient = super.getUniformLocation("fogGradient");
        location_fogApplied = super.getUniformLocation("fogApplied");
        location_cameraPosition = super.getUniformLocation("cameraPosition");

        location_lightPositions = new int[LightSettings.MAX_LIGHTS];
        location_lightColors = new int[LightSettings.MAX_LIGHTS];

        for (int i = 0; i < LightSettings.MAX_LIGHTS; i++) {
            String lightPositionName = String.format("lightPositions[%d]", i);
            String lightColorName = String.format("lightColors[%d]", i);
            location_lightPositions[i] = super.getUniformLocation(lightPositionName);
            location_lightColors[i] = super.getUniformLocation(lightColorName);
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_viewMatrix, matrix);
    }

    @Override
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

            super.loadVector3fToUniform(location_lightPositions[i], positionToLoad);
            super.loadVector3fToUniform(location_lightColors[i], colorToLoad);
        }
    }

    @Override
    public void loadShineSettings(ShineSettings shineSettings) {
        float reflectivity = 0;
        float shineDamper = 0;

        if (shineSettings != null) {
            reflectivity = shineSettings.reflectivity;
            shineDamper = shineSettings.shineDamper;
        }

        super.loadFloatToUniform(location_reflectivity, reflectivity);
        super.loadFloatToUniform(location_shineDamper, shineDamper);
    }

    @Override
    public void loadMinBrightness(float minBrightness) {
        super.loadFloatToUniform(location_minBrightness, minBrightness);
    }

    @Override
    public void loadNumLightsInUse(int numLightsInUse) {
        if (numLightsInUse > LightSettings.MAX_LIGHTS)
            throw new RuntimeException();

        super.loadIntegerToUniform(location_numLightsInUse, numLightsInUse);
    }

    @Override
    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector3fToUniform(location_skyColor, skyColor);
    }

    @Override
    public void loadFogType(boolean useSphericalFog) {
        super.loadBooleanToUniform(location_fogType, useSphericalFog);
    }

    @Override
    public void loadFogDensity(float fogDensity) {
        super.loadFloatToUniform(location_fogDensity, fogDensity);
    }

    @Override
    public void loadFogGradient(float fogGradient) {
        super.loadFloatToUniform(location_fogGradient, fogGradient);
    }

    @Override
    public void loadFogApplied(boolean fogApplied) {
        super.loadBooleanToUniform(location_fogApplied, fogApplied);
    }

    @Override
    public void loadCameraPosition(Vector3f cameraPosition) {
        super.loadVector3fToUniform(location_cameraPosition, cameraPosition);
    }

}
