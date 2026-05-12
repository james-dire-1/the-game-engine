package templates.rendering;

import com.james.renderEngine.models.Model;
import com.james.renderEngine.texturing.ShineSettings;
import newStuff.evenNewer.ModelLoader;
import newStuff.evenNewer.SingleMesh;

import java.util.List;
import java.util.Random;

public class ModelBank {

    private static Model stallModel;
    private static Model abstractArtModel;
    private static Model colorAbstractArtModel;
    private static Model wallModel;
    private static Model testEnvironmentModel;
    private static Model[] desertEnvironmentModels;
    private static Model desertEnvironmentModel;
    private static Model[] beachEnvironmentModels;

    public static Model getStall() {
        if (stallModel == null) {
            ModelLoader loader = ModelLoader.get("/stall.obj");
            stallModel = new Model(loader.vertexPositions(), loader.indices());
            stallModel.setTextureAndTextureCoords("/textures/stall.png", loader.textureCoords());
            stallModel.setNormals(loader.normals());
            stallModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return stallModel;
    }

    public static Model getAbstractArt() {
        if (abstractArtModel == null) {
            ModelLoader loader = ModelLoader.get("/abstract-art.dae");
            abstractArtModel = new Model(loader.vertexPositions(), loader.indices());
            abstractArtModel.setTextureAndTextureCoords("/textures/stall.png", loader.textureCoords());
            abstractArtModel.setNormals(loader.normals());
            abstractArtModel.setRenderer(Renderers.basicRenderer);
        }

        return abstractArtModel;
    }

    private static final Random r = new Random();
    public static Model getColorAbstractArt() {
        if (colorAbstractArtModel == null) {
            ModelLoader loader = ModelLoader.get("/abstract-art.dae");
            colorAbstractArtModel = new Model(loader.vertexPositions(), loader.indices());

            int uniqueVertexCount = colorAbstractArtModel.getUniqueVertexCount();
            float[] colors = new float[uniqueVertexCount * 3];
            for (int i = 0; i < uniqueVertexCount; i++) {
                colors[i * 3] = r.nextFloat();
                colors[i * 3 + 1] = r.nextFloat();
                colors[i * 3 + 2] = r.nextFloat();
            }
            colorAbstractArtModel.setColors(colors);

            colorAbstractArtModel.setNormals(loader.normals());
            colorAbstractArtModel.setShineSettings(new ShineSettings(1, 200));
            colorAbstractArtModel.setRenderer(Renderers.colorModelRenderer);
        }

        return colorAbstractArtModel;
    }

    public static Model getWall() {
        if (wallModel == null) {
            ModelLoader loader = ModelLoader.get("/one-sided-wall.dae");
            wallModel = new Model(loader.vertexPositions(), loader.indices());
            wallModel.setTextureAndTextureCoords("/textures/cobblestone-wall.png", loader.textureCoords());
            wallModel.setNormals(loader.normals());
            wallModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return wallModel;
    }

    public static Model getTestEnvironment() {
        if (testEnvironmentModel == null) {
            ModelLoader loader = ModelLoader.get("/test-environment.dae");
            testEnvironmentModel = new Model(loader.vertexPositions(), loader.indices());
            testEnvironmentModel.setTextureAndTextureCoords("/textures/white-image.png", loader.textureCoords());
            testEnvironmentModel.setNormals(loader.normals());
            testEnvironmentModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return testEnvironmentModel;
    }

    public static Model[] getDesertEnvironmentManyModels() {
        if (desertEnvironmentModels == null) {
            ModelLoader loader = ModelLoader.get("/desert-2.dae");
            List<SingleMesh> singleMeshes = loader.getAllSingleMeshes();
            desertEnvironmentModels = new Model[singleMeshes.size()];

            for (int i = 0; i < desertEnvironmentModels.length; i++) {
                SingleMesh singleMesh = singleMeshes.get(i);
                Model model = new Model(singleMesh.vertexPositions, singleMesh.indices);
                model.setTextureAndTextureCoords("/textures/stone-ground.png", singleMesh.textureCoords);
                model.setNormals(singleMesh.normals);
                model.setRenderer(Renderers.texturedModelRenderer);
                desertEnvironmentModels[i] = model;
            }
        }

        return desertEnvironmentModels;
    }

    public static Model getDesertEnvironmentOneModel() {
        if (desertEnvironmentModel == null) {
            ModelLoader loader = ModelLoader.get("/desert-2.dae");
            desertEnvironmentModel = new Model(loader.vertexPositionsForAllSingleMeshes(), loader.indicesForAllSingleMeshes());
            desertEnvironmentModel.setTextureAndTextureCoords("/textures/stone-ground.png", loader.textureCoordsForAllSingleMeshes());
            desertEnvironmentModel.setNormals(loader.normalsForAllSingleMeshes());
            desertEnvironmentModel.setRenderer(Renderers.texturedModelRenderer);
        }

        return desertEnvironmentModel;
    }

    public static Model[] getBeachEnvironmentManyModels() {
        if (beachEnvironmentModels == null) {
            ModelLoader loader = ModelLoader.get("/beach-scene.dae");
            List<SingleMesh> singleMeshes = loader.getAllSingleMeshes();
            beachEnvironmentModels = new Model[singleMeshes.size()];

            for (int i = 0; i < beachEnvironmentModels.length; i++) {
                SingleMesh singleMesh = singleMeshes.get(i);
                Model model = new Model(singleMesh.vertexPositions, singleMesh.indices);

                String name = singleMesh.name;
                String texturePath;

                if (name.startsWith("_Port") || name.startsWith("_Platform"))
                    texturePath = "/textures/beach-scene/palm-bark-and-wood.png";
                else if (name.startsWith("_Purple") || name.startsWith("_PurpleChair"))
                    texturePath = "/textures/beach-scene/purple.png";
                else if (name.startsWith("_Yellow") || name.startsWith("_YellowChair"))
                    texturePath = "/textures/beach-scene/yellow.png";
                else if (name.startsWith("_Red")  || name.startsWith("_RedChair"))
                    texturePath = "/textures/beach-scene/red.png";
                else if (name.startsWith("_Bark"))
                    texturePath = "/textures/beach-scene/palm-bark.png";
                else if (name.startsWith("_PalmTree"))
                    texturePath = "/textures/beach-scene/palm-tree.png";
                else if (name.startsWith("_Campfire"))
                    texturePath = "/textures/beach-scene/wood.png";
                else if (name.startsWith("_Pebble"))
                    texturePath = "/textures/beach-scene/stone.png";
                else if (name.startsWith("_PebblePatch"))
                    texturePath = "/textures/beach-scene/pebbles.png";
                else if (name.startsWith("_Sand"))
                    texturePath = "/textures/beach-scene/sand.png";
                else if (name.startsWith("_Water"))
                    texturePath = "/textures/beach-scene/water.png";
                else if (name.startsWith("_Monkeys"))
                    texturePath = "/textures/beach-scene/palm-bark-and-wood.png";
                else if (name.startsWith("_TallRock"))
                    texturePath = "/textures/beach-scene/black-and-pink-stone.png";
                else
                    texturePath = "/textures/white-image.png";

                if (name.startsWith("_Purple") || name.startsWith("_Yellow") || name.startsWith("_Red") ||
                        name.startsWith("_PalmTree")) {
                    model.disableCulling();
                }

                model.setTextureAndTextureCoords(texturePath, singleMesh.textureCoords);
                model.setNormals(singleMesh.normals);
                model.setRenderer(Renderers.texturedModelRenderer);
                beachEnvironmentModels[i] = model;
            }
        }

        return beachEnvironmentModels;
    }

}
