package com.james.renderEngine.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class BasicShader extends Shader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/basicVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/basicFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    private int location_time;

    public BasicShader() {
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
        location_time = super.getUniformLocation("time");
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

    public void loadTime() {
        double seconds = (System.nanoTime()/1_000_000_000.0);
        float fraction = (float) (seconds - Math.floor(seconds));
        super.loadFloatToUniform(location_time, fraction);
    }

}
