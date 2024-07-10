package templates.rendering;

import com.james.renderEngine.models.Model;
import com.james.common.tools.ModelLoader;

public class ModelBank {

    private static Model stallModel;
    private static Model abstractArtModel;
    private static Model wallModel;
    private static Model testEnvironmentModel;

    public static Model getStall() {
        if (stallModel == null) {
            ModelLoader loader = ModelLoader.get("res/stall.obj");
            stallModel = new Model(loader.vertexPositions, loader.indices);
            stallModel.setTextureAndTextureCoords("/stall.png", loader.textureCoords);
            stallModel.setNormals(loader.normals);
            stallModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return stallModel;
    }

    public static Model getAbstractArt() {
        if (abstractArtModel == null) {
            ModelLoader loader = ModelLoader.get("res/abstract_art.dae");
            abstractArtModel = new Model(loader.vertexPositions, loader.indices);
            abstractArtModel.setTextureAndTextureCoords("/stall.png", loader.textureCoords);
            abstractArtModel.setNormals(loader.normals);
            abstractArtModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return abstractArtModel;
    }

    public static Model getWall() {
        if (wallModel == null) {
            ModelLoader loader = ModelLoader.get("res/one-sided-wall5.dae");
            wallModel = new Model(loader.vertexPositions, loader.indices);
            wallModel.setTextureAndTextureCoords("/cobblestone_wall.png", loader.textureCoords);
            wallModel.setNormals(loader.normals);
            wallModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return wallModel;
    }

    public static Model getTestEnvironment() {
        if (testEnvironmentModel == null) {
            ModelLoader loader = ModelLoader.get("res/test_environment_7.dae");
            testEnvironmentModel = new Model(loader.vertexPositions, loader.indices);
            testEnvironmentModel.setTextureAndTextureCoords("/white_image.png", loader.textureCoords);
            testEnvironmentModel.setNormals(loader.normals);
            testEnvironmentModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return testEnvironmentModel;
    }

}
