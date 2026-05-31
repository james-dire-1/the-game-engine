package com.james.renderEngine.shaders.models;

import com.james.renderEngine.shaders.models.interfaces.IFogShader;
import com.james.renderEngine.shaders.models.interfaces.ILightShader;
import com.james.renderEngine.shaders.models.interfaces.IModelShader;
import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.shaders.models.components.FogShaderFeature;
import com.james.renderEngine.shaders.models.components.LightShaderFeature;
import com.james.renderEngine.shaders.models.components.ModelShaderFeature;

public class ColorModelShader extends Shader implements IModelShader, ILightShader, IFogShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/colorModelVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/colorModelFragmentShader.txt";

    public final ModelShaderFeature modelShaderFeature = new ModelShaderFeature(this);
    public final LightShaderFeature lightShaderFeature = new LightShaderFeature(this);
    public final FogShaderFeature fogShaderFeature = new FogShaderFeature(this);

    @Override public ModelShaderFeature model() { return modelShaderFeature; }
    @Override public LightShaderFeature light() { return lightShaderFeature; }
    @Override public FogShaderFeature fog() { return fogShaderFeature; }

    public ColorModelShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "color");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getUniformLocations() {
        modelShaderFeature.getUniformLocations();
        lightShaderFeature.getUniformLocations();
        fogShaderFeature.getUniformLocations();
    }

}
