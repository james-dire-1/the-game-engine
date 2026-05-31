package com.james.renderEngine.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends Shader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/particleVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/particleFragmentShader.txt";

    private int location_viewModelMatrix;
    private int location_projectionMatrix;

    public ParticleShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
    }

    @Override
    protected void getUniformLocations() {
        location_viewModelMatrix = super.getUniformLocation("viewModelMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    public void loadViewModelMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_viewModelMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

}
