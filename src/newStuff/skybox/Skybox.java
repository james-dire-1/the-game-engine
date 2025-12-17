package newStuff.skybox;

import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.utilities.VertexUtilityArrays;

public class Skybox {

    private static RawModel rawModel;
    public static RawModel getRawModel() { return rawModel; }

    public final CubeMapTexture texture;

    public boolean unmoving = false;

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
