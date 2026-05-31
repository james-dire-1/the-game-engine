package com.james.renderEngine.shaders.models.components;

import com.james.renderEngine.shaders.Shader;
import org.lwjgl.util.vector.Vector3f;

public class FogShaderFeature implements ShaderFeature {

    private final Shader shader;

    private int location_skyColor;
    private int location_fogType;
    private int location_fogDensity;
    private int location_fogGradient;
    private int location_fogApplied;
    private int location_cameraPosition;

    public FogShaderFeature(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void getUniformLocations() {
        location_skyColor = shader.getUniformLocation("skyColor");
        location_fogType = shader.getUniformLocation("fogType");
        location_fogDensity = shader.getUniformLocation("fogDensity");
        location_fogGradient = shader.getUniformLocation("fogGradient");
        location_fogApplied = shader.getUniformLocation("fogApplied");
        location_cameraPosition = shader.getUniformLocation("cameraPosition");
    }

    public void loadSkyColor(Vector3f skyColor) {
        shader.loadVector3fToUniform(location_skyColor, skyColor);
    }

    public void loadFogType(boolean useSphericalFog) {
        shader.loadBooleanToUniform(location_fogType, useSphericalFog);
    }

    public void loadFogDensity(float fogDensity) {
        shader.loadFloatToUniform(location_fogDensity, fogDensity);
    }

    public void loadFogGradient(float fogGradient) {
        shader.loadFloatToUniform(location_fogGradient, fogGradient);
    }

    public void loadFogApplied(boolean fogApplied) {
        shader.loadBooleanToUniform(location_fogApplied, fogApplied);
    }

    public void loadCameraPosition(Vector3f cameraPosition) {
        shader.loadVector3fToUniform(location_cameraPosition, cameraPosition);
    }

}
