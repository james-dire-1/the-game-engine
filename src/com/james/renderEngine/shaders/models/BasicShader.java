package com.james.renderEngine.shaders.models;

import com.james.renderEngine.shaders.models.interfaces.IModelShader;
import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.shaders.models.components.ModelShaderFeature;

public class BasicShader extends Shader implements IModelShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/basicVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/basicFragmentShader.txt";

    public final ModelShaderFeature modelShaderFeature = new ModelShaderFeature(this);
    private int location_time;

    @Override public ModelShaderFeature model() { return modelShaderFeature; }

    public BasicShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getUniformLocations() {
        modelShaderFeature.getUniformLocations();
        location_time = super.getUniformLocation("time");
    }

    public void loadTime() {
        double seconds = (System.nanoTime() / 1_000_000_000.0);
        float fraction = (float) (seconds - Math.floor(seconds));
        super.loadFloatToUniform(location_time, fraction);
    }

}
