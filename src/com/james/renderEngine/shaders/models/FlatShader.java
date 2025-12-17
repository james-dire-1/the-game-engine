package com.james.renderEngine.shaders.models;

import com.james.renderEngine.shaders.Shader;
import org.lwjgl.util.vector.Matrix4f;

public class FlatShader extends Shader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/flatVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/flatFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

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

}
