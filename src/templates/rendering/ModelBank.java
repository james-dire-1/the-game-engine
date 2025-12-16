package templates.rendering;

import com.james.renderEngine.models.Model;
import com.james.common.tools.ModelLoader;
import newStuff.ShineSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ModelBank {

    private static Model stallModel;
    private static Model abstractArtModel;
    private static Model colorAbstractArtModel;
    private static Model wallModel;
    private static Model testEnvironmentModel;

    public static Model getStall() {
        if (stallModel == null) {
            ModelLoader loader = ModelLoader.get("/stall.obj");
            stallModel = new Model(loader.vertexPositions, loader.indices);
            stallModel.setTextureAndTextureCoords("/textures/stall.png", loader.textureCoords);
            stallModel.setNormals(loader.normals);
            stallModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return stallModel;
    }

    public static Model getAbstractArt() {
        if (abstractArtModel == null) {
            ModelLoader loader = ModelLoader.get("/abstract-art.dae");
            abstractArtModel = new Model(loader.vertexPositions, loader.indices);
            abstractArtModel.setTextureAndTextureCoords("/textures/stall.png", loader.textureCoords);
            abstractArtModel.setNormals(loader.normals);
            abstractArtModel.setRenderer(Renderers.basicRenderer);
        }

        return abstractArtModel;
    }

    private static final Random r = new Random();
    public static Model getColorAbstractArt() {
        if (colorAbstractArtModel == null) {
            ModelLoader loader = ModelLoader.get("/abstract-art.dae");
            colorAbstractArtModel = new Model(loader.vertexPositions, loader.indices);

            int uniqueVertexCount = colorAbstractArtModel.getUniqueVertexCount();
            float[] colors = new float[uniqueVertexCount * 3];
            for (int i = 0; i < uniqueVertexCount; i++) {
                colors[i * 3] = r.nextFloat();
                colors[i * 3 + 1] = r.nextFloat();
                colors[i * 3 + 2] = r.nextFloat();
            }
            colorAbstractArtModel.setColors(colors);

            colorAbstractArtModel.setNormals(loader.normals);
            colorAbstractArtModel.setShineSettings(new ShineSettings(1, 200));
            colorAbstractArtModel.setRenderer(Renderers.colorModelRenderer);
        }

        return colorAbstractArtModel;
    }

    public static Model getWall() {
        if (wallModel == null) {
            ModelLoader loader = ModelLoader.get("/one-sided-wall.dae");
            wallModel = new Model(loader.vertexPositions, loader.indices);
            wallModel.setTextureAndTextureCoords("/textures/cobblestone-wall.png", loader.textureCoords);
            wallModel.setNormals(loader.normals);
            wallModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return wallModel;
    }

    public static Model getTestEnvironment() {
        if (testEnvironmentModel == null) {
            ModelLoader loader = ModelLoader.get("/test-environment.dae");
            testEnvironmentModel = new Model(loader.vertexPositions, loader.indices);
            testEnvironmentModel.setTextureAndTextureCoords("/textures/white-image.png", loader.textureCoords);
            testEnvironmentModel.setNormals(loader.normals);
            testEnvironmentModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return testEnvironmentModel;
    }

}
