package com.james.renderEngine.shaders;

import com.james.renderEngine.ui.Gui;
import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends Shader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/guiFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_renderingMode;
    private int location_singleColor;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "color");
    }

    @Override
    protected void getUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_renderingMode = super.getUniformLocation("renderingMode");
        location_singleColor = super.getUniformLocation("singleColor");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

    public void loadRenderingMode(Gui.RenderingMode renderingMode) {
        float toLoad = 0;
        if (renderingMode == Gui.RenderingMode.Texture) toLoad = 1;
        else if (renderingMode == Gui.RenderingMode.ColorGradient) toLoad = 2;
        else if (renderingMode == Gui.RenderingMode.SingleColor) toLoad = 3;
        else if (renderingMode == Gui.RenderingMode.Text) toLoad = 4;

        super.loadFloatToUniform(location_renderingMode, toLoad);
    }

    public void loadSingleColor(float[] singleColor) {
        super.loadVector3fToUniform(location_singleColor, singleColor[0], singleColor[1], singleColor[2]);
    }

}
