package com.james.renderEngine.visuals;

import com.james.renderEngine.models.Model;
import com.james.renderEngine.shaders.models.TextureBlendModelShader;
import com.james.renderEngine.texturing.TextureBlendPack;

import static org.lwjgl.opengl.GL30.*;

public class TextureSettings {

    public static void loadModelSettingsForTexturedModel(Model model) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture().id);
    }

    public static void loadSettingsFirstTimeForTextureBlendModel(TextureBlendModelShader shader) {
        shader.loadTextureUnitsToSamplers();
    }

    public static void loadModelSettingsForTextureBlendModel(TextureBlendModelShader shader, Model model) {
        TextureBlendPack texturePack = model.getTextureBlendPack();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturePack.getBdChannelTexture().id);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, texturePack.getRChannelTexture().id);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, texturePack.getGChannelTexture().id);
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, texturePack.getBChannelTexture().id);
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, texturePack.getBlendMap().id);

        shader.loadTiledTextureCoordsMultiplier(texturePack.tiledTextureCoordsMultiplier);
    }

}
