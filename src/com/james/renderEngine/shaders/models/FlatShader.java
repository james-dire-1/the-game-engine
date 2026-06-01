package com.james.renderEngine.shaders.models;

import com.james.renderEngine.shaders.models.interfaces.IFogShader;
import com.james.renderEngine.shaders.models.interfaces.IModelShader;
import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.shaders.models.components.FogShaderFeature;
import com.james.renderEngine.shaders.models.components.ModelShaderFeature;

public class FlatShader extends Shader implements IModelShader, IFogShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/flatVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/flatFragmentShader.txt";

    private final ModelShaderFeature modelShaderFeature = new ModelShaderFeature(this);
    private final FogShaderFeature fogShaderFeature = new FogShaderFeature(this);

    @Override public ModelShaderFeature model() { return modelShaderFeature; }
    @Override public FogShaderFeature fog() { return fogShaderFeature; }

    public FlatShader() {
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
        fogShaderFeature.getUniformLocations();
    }

}
