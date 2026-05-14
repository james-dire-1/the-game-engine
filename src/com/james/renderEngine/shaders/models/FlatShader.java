package com.james.renderEngine.shaders.models;

import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.rendering.interfaces.IFogShader;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class FlatShader extends Shader implements IFogShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/flatVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/flatFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_skyColor;
    private int location_fogType;
    private int location_fogDensity;
    private int location_fogGradient;
    private int location_fogApplied;
    private int location_cameraPosition;

    public FlatShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
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
