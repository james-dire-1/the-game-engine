package com.james.renderEngine.shaders.models;

import com.james.renderEngine.shaders.Shader;
import com.james.renderEngine.shaders.models.components.FogShaderFeature;
import com.james.renderEngine.shaders.models.components.LightShaderFeature;
import com.james.renderEngine.shaders.models.components.ModelShaderFeature;
import com.james.renderEngine.shaders.models.interfaces.IFogShader;
import com.james.renderEngine.shaders.models.interfaces.ILightShader;
import com.james.renderEngine.shaders.models.interfaces.IModelShader;

public class TextureBlendModelShader extends Shader implements IModelShader, ILightShader, IFogShader {

    private static final String VERTEX_FILE = "/com/james/renderEngine/glsl/textureBlendVertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/james/renderEngine/glsl/textureBlendFragmentShader.txt";

    private static final int BD_CHANNEL_TEXTURE_TEXTURE_UNIT = 0;
    private static final int R_CHANNEL_TEXTURE_TEXTURE_UNIT = 1;
    private static final int G_CHANNEL_TEXTURE_TEXTURE_UNIT = 2;
    private static final int B_CHANNEL_TEXTURE_TEXTURE_UNIT = 3;
    private static final int BLEND_MAP_TEXTURE_UNIT = 4;

    private final ModelShaderFeature modelShaderFeature = new ModelShaderFeature(this);
    private final LightShaderFeature lightShaderFeature = new LightShaderFeature(this);
    private final FogShaderFeature fogShaderFeature = new FogShaderFeature(this);
    private int location_bdChannelTexture;
    private int location_rChannelTexture;
    private int location_gChannelTexture;
    private int location_bChannelTexture;
    private int location_blendMap;
    private int location_tiledTextureCoordsMultiplier;

    @Override public ModelShaderFeature model() { return modelShaderFeature; }
    @Override public LightShaderFeature light() { return lightShaderFeature; }
    @Override public FogShaderFeature fog() { return fogShaderFeature; }

    public TextureBlendModelShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getUniformLocations() {
        modelShaderFeature.getUniformLocations();
        lightShaderFeature.getUniformLocations();
        fogShaderFeature.getUniformLocations();
        location_bdChannelTexture = super.getUniformLocation("bdChannelTexture");
        location_rChannelTexture = super.getUniformLocation("rChannelTexture");
        location_gChannelTexture = super.getUniformLocation("gChannelTexture");
        location_bChannelTexture = super.getUniformLocation("bChannelTexture");
        location_blendMap = super.getUniformLocation("blendMap");
        location_tiledTextureCoordsMultiplier = super.getUniformLocation("tiledTextureCoordsMultiplier");
    }

    public void loadTextureUnitsToSamplers() {
        super.loadIntegerToUniform(location_bdChannelTexture, BD_CHANNEL_TEXTURE_TEXTURE_UNIT);
        super.loadIntegerToUniform(location_rChannelTexture, R_CHANNEL_TEXTURE_TEXTURE_UNIT);
        super.loadIntegerToUniform(location_gChannelTexture, G_CHANNEL_TEXTURE_TEXTURE_UNIT);
        super.loadIntegerToUniform(location_bChannelTexture, B_CHANNEL_TEXTURE_TEXTURE_UNIT);
        super.loadIntegerToUniform(location_blendMap, BLEND_MAP_TEXTURE_UNIT);
    }

    public void loadTiledTextureCoordsMultiplier(float tiledTextureCoordsMultiplier) {
        super.loadFloatToUniform(location_tiledTextureCoordsMultiplier, tiledTextureCoordsMultiplier);
    }

}
