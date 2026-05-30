package com.james.renderEngine.shaders.models;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.texturing.ShineSettings;
import com.james.renderEngine.shaders.interfaces.IFogShader;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ColorModelShader extends Shader implements IFogShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/colorModelVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/colorModelFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_reflectivity;
    private int location_shineDamper;
    private int location_minBrightness;
    private int location_skyColor;
    private int location_fogType;
    private int location_fogDensity;
    private int location_fogGradient;
    private int location_fogApplied;
    private int location_cameraPosition;

    public ColorModelShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "color");
    }

    @Override
    protected void getUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_minBrightness = super.getUniformLocation("minBrightness");
        location_skyColor = super.getUniformLocation("skyColor");
        location_fogType = super.getUniformLocation("fogType");
        location_fogDensity = super.getUniformLocation("fogDensity");
        location_fogGradient = super.getUniformLocation("fogGradient");
        location_fogApplied = super.getUniformLocation("fogApplied");
        location_cameraPosition = super.getUniformLocation("cameraPosition");
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

    public void loadLight(Light light) {
        super.loadVector3fToUniform(location_lightPosition, light.getPosition());
        super.loadVector3fToUniform(location_lightColor, light.getColor());
    }

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

    public void loadMinBrightness(float minBrightness) {
        super.loadFloatToUniform(location_minBrightness, minBrightness);
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
