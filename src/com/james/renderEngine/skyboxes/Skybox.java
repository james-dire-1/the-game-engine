package com.james.renderEngine.skyboxes;

import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.utilities.VertexUtilityArrays;
import com.james.renderEngine.texturing.CubeMapTexture;

/**
 * Represents a visual skybox.
 */
public class Skybox {

    private static RawModel rawModel;
    public static RawModel getRawModel() { return rawModel; }

    public final CubeMapTexture texture;

    public boolean unmoving = false;

    /**
     * Creates a skybox from the associated name of a cube map texture.
     */
    public Skybox(String textureName) {
        if (rawModel == null) {
            rawModel = new RawModel(
                    VertexUtilityArrays.defaultSkyboxVertexPositions,
                    VertexUtilityArrays.defaultSkyboxIndices);
        }

        this.texture = CubeMapTexture.get(textureName);
    }

    public static Skybox currentSkybox;

}
