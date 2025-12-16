package com.james.renderEngine.shaders;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.texturing.ShineSettings;
import org.lwjgl.util.vector.Matrix4f;

public class TexturedModelShader extends Shader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/texturedModelVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/texturedModelFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_reflectivity;
    private int location_shineDamper;
    private int location_minBrightness;

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
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_minBrightness = super.getUniformLocation("minBrightness");
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

}
