package com.james.renderEngine.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends Shader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
    }

    @Override
    protected void getUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

}
