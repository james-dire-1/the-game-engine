package com.james.renderEngine.shaders.models.components;

import com.james.renderEngine.shaders.Shader;
import org.lwjgl.util.vector.Matrix4f;

public class ModelShaderFeature implements ShaderFeature {

    private final Shader shader;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public ModelShaderFeature(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void getUniformLocations() {
        location_transformationMatrix = shader.getUniformLocation("transformationMatrix");
        location_projectionMatrix = shader.getUniformLocation("projectionMatrix");
        location_viewMatrix = shader.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        shader.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        shader.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        shader.loadMatrixToUniform(location_viewMatrix, matrix);
    }

}
