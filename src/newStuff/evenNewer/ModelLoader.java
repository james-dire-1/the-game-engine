package newStuff.evenNewer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import templates.common.GlobalConstants;

import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.assimp.Assimp.*;

// TODO: 2026-05-10 Consider adding a way to not load textureCoords and normals from the file
// TODO: 2026-05-10 This is helpful when doing server-side things
// https://www.youtube.com/watch?v=eqlwamit0vU&t=883s
public class ModelLoader {

    private static final String CUSTOM_NAME_START_CHAR = "@";
    private static final String END_STRING = "-mesh";

    private Map<String, SingleMesh> namedSingleMeshes = null;
    private final SingleMesh[] otherSingleMeshes;

    private ModelLoader(String path) {
        String fullPath = GlobalConstants.MODELS_BASE_DIRECTORY + path;
        AIScene scene = aiImportFile(fullPath,aiProcess_Triangulate | aiProcess_FlipUVs |
                aiProcess_JoinIdenticalVertices);

        if (scene == null)
            System.err.println("Assimp import failed! " + aiGetErrorString());

        Objects.requireNonNull(scene);
        PointerBuffer meshes = scene.mMeshes();
        Objects.requireNonNull(meshes);

        List<SingleMesh> otherSingleMeshesList = new ArrayList<>();

        for (int i = 0; i < meshes.limit(); i++) {
            AIMesh mesh = AIMesh.create(meshes.get(i));
            SingleMesh singleMesh = processMesh(mesh);
            String name = mesh.mName().dataString();

            if (name.startsWith(CUSTOM_NAME_START_CHAR)) {
                if (namedSingleMeshes == null) {
                    namedSingleMeshes = new HashMap<>();
                }

                name = name.replace(CUSTOM_NAME_START_CHAR, "").replace(END_STRING, "");
                namedSingleMeshes.put(name, singleMesh);
            } else {
                otherSingleMeshesList.add(singleMesh);
            }
        }

        otherSingleMeshes = otherSingleMeshesList.toArray(new SingleMesh[0]);
    }

    private SingleMesh processMesh(AIMesh mesh) {
        AIVector3D.Buffer vectors = mesh.mVertices();
        AIVector3D.Buffer coords = mesh.mTextureCoords(0);
        AIVector3D.Buffer norms = mesh.mNormals();
        AIFace.Buffer faces = mesh.mFaces();

        Objects.requireNonNull(vectors);
        Objects.requireNonNull(coords);
        Objects.requireNonNull(norms);
        Objects.requireNonNull(faces);

        List<Float> vertexPositionsList = new ArrayList<>();
        List<Float> textureCoordsList = new ArrayList<>();
        List<Float> normalsList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        for (int i = 0; i < vectors.limit(); i++) {
            AIVector3D vector = vectors.get(i);

            vertexPositionsList.add(vector.x());
            vertexPositionsList.add(vector.y());
            vertexPositionsList.add(vector.z());
        }

        for (int i = 0; i < coords.limit(); i++) {
            AIVector3D coord = coords.get(i);

            textureCoordsList.add(coord.x());
            textureCoordsList.add(coord.y());
        }

        for (int i = 0; i < norms.limit(); i++) {
            AIVector3D norm = norms.get(i);

            normalsList.add(norm.x());
            normalsList.add(norm.y());
            normalsList.add(norm.z());
        }

        for (int i = 0; i < faces.limit(); i++) {
            AIFace face = faces.get(i);
            IntBuffer indicesForFace = face.mIndices();

            indicesList.add(indicesForFace.get(0));
            indicesList.add(indicesForFace.get(1));
            indicesList.add(indicesForFace.get(2));
        }

        float[] vertexPositions = new float[vertexPositionsList.size()];
        for (int i = 0; i < vertexPositionsList.size(); i++) {
            vertexPositions[i] = vertexPositionsList.get(i);
        }

        float[] textureCoords = new float[textureCoordsList.size()];
        for (int i = 0; i < textureCoordsList.size(); i++) {
            textureCoords[i] = textureCoordsList.get(i);
        }

        float[] normals = new float[normalsList.size()];
        for (int i = 0; i < normalsList.size(); i++) {
            normals[i] = normalsList.get(i);
        }

        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }

        return new SingleMesh(vertexPositions, textureCoords, normals, indices);
    }

    public float[] vertexPositions() { return otherSingleMeshes[0].vertexPositions; }
    public float[] textureCoords() { return otherSingleMeshes[0].textureCoords; }
    public float[] normals() { return otherSingleMeshes[0].normals; }
    public int[] indices() { return otherSingleMeshes[0].indices; }

    private float[] vertexPositionsAllMeshes;
    private float[] textureCoordsAllMeshes;
    private float[] normalsAllMeshes;
    private int[] indicesAllMeshes;

    public float[] vertexPositionsForAllSingleMeshes() {
        if (vertexPositionsAllMeshes == null)
            vertexPositionsAllMeshes = ModelLoaderHelper.vertexPositionsAllMeshes(this);
        return vertexPositionsAllMeshes;
    }

    public float[] textureCoordsForAllSingleMeshes() {
        if (textureCoordsAllMeshes == null)
            textureCoordsAllMeshes = ModelLoaderHelper.textureCoordsAllMeshes(this);
        return textureCoordsAllMeshes;
    }

    public float[] normalsForAllSingleMeshes() {
        if (normalsAllMeshes == null)
            normalsAllMeshes = ModelLoaderHelper.normalsAllMeshes(this);
        return normalsAllMeshes;
    }

    public int[] indicesForAllSingleMeshes() {
        if (indicesAllMeshes == null)
            indicesAllMeshes = ModelLoaderHelper.indicesAllMeshes(this);
        return indicesAllMeshes;
    }

    public SingleMesh getSingleMesh(String name) {
        return namedSingleMeshes.get(name);
    }

    public SingleMesh[] getOtherSingleMeshes() {
        return otherSingleMeshes;
    }

    private List<SingleMesh> allSingleMeshes;
    public List<SingleMesh> getAllSingleMeshes() {
        if (allSingleMeshes == null) {
            allSingleMeshes = new ArrayList<>();

            if (namedSingleMeshes != null)
                allSingleMeshes.addAll(namedSingleMeshes.values());
            allSingleMeshes.addAll(Arrays.asList(otherSingleMeshes));
        }

        return allSingleMeshes;
    }

    private static final Map<String, ModelLoader> modelLoaderMap = new HashMap<>();

    public static void init(String... modelFilePaths) {
        for (String path : modelFilePaths) {
            ModelLoader loader = new ModelLoader(path);
            modelLoaderMap.put(path, loader);
        }
    }

    public static ModelLoader get(String path) {
        return modelLoaderMap.get(path);
    }

}
