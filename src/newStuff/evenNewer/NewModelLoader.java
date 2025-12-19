package newStuff.evenNewer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import templates.common.GlobalConstants;

import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.assimp.Assimp.*;

// https://www.youtube.com/watch?v=eqlwamit0vU&t=883s
public class NewModelLoader {

    private final Map<String, SingleMesh> namedSingleMeshes = new HashMap<>();
    private final List<SingleMesh> otherSingleMeshes = new ArrayList<>();

    private NewModelLoader(String path) {
        System.out.println("Loading " + path);

        String fullPath = GlobalConstants.MODELS_BASE_DIRECTORY + path;
        AIScene scene = aiImportFile(fullPath,aiProcess_Triangulate | aiProcess_FlipUVs |
                aiProcess_JoinIdenticalVertices);

        if (scene == null) {
            System.err.println("Assimp import failed! " + aiGetErrorString());
        }

        Objects.requireNonNull(scene);
        PointerBuffer buffer = scene.mMeshes();
        Objects.requireNonNull(buffer);

        System.out.println("scene.mNumMeshes() = " + scene.mNumMeshes());

        for (int i = 0; i < buffer.limit(); i++) {
            AIMesh mesh = AIMesh.create(buffer.get(i));
            processMesh(mesh);
        }
    }

    private void processMesh(AIMesh mesh) {
        // Vertices
        AIVector3D.Buffer vectors = mesh.mVertices();
        Objects.requireNonNull(vectors);
        List<Float> vertexPositionsList = new ArrayList<>();

        for (int i = 0; i < vectors.limit(); i++) {
            AIVector3D vector = vectors.get(i);

            vertexPositionsList.add(vector.x());
            vertexPositionsList.add(vector.y());
            vertexPositionsList.add(vector.z());
        }
        float[] vertexPositions = new float[vertexPositionsList.size()];
        for (int i = 0; i < vertexPositionsList.size(); i++) {
            vertexPositions[i] = vertexPositionsList.get(i);
        }

        // Texture Coords
        AIVector3D.Buffer coords = mesh.mTextureCoords(0);
        Objects.requireNonNull(coords);
        List<Float> textureCoordsList = new ArrayList<>();

        for (int i = 0; i < coords.limit(); i++) {
            AIVector3D coord = coords.get(i);

            textureCoordsList.add(coord.x());
            textureCoordsList.add(coord.y());
        }
        float[] textureCoords = new float[textureCoordsList.size()];
        for (int i = 0; i < textureCoordsList.size(); i++) {
            textureCoords[i] = textureCoordsList.get(i);
        }

        // Normals
        AIVector3D.Buffer norms = mesh.mNormals();
        Objects.requireNonNull(norms);
        List<Float> normalsList = new ArrayList<>();

        for (int i = 0; i < norms.limit(); i++) {
            AIVector3D norm = norms.get(i);

            normalsList.add(norm.x());
            normalsList.add(norm.y());
            normalsList.add(norm.z());
        }
        float[] normals = new float[normalsList.size()];
        for (int i = 0; i < normalsList.size(); i++) {
            normals[i] = normalsList.get(i);
        }

        // Indices
        AIFace.Buffer faces = mesh.mFaces();
        List<Integer> indicesList = new ArrayList<>();

        for (int i = 0; i < faces.limit(); i++) {
            AIFace face = faces.get(i);
            IntBuffer indicesForFace = face.mIndices();
            indicesList.add(indicesForFace.get(0));
            indicesList.add(indicesForFace.get(1));
            indicesList.add(indicesForFace.get(2));
        }
        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }

        SingleMesh singleMesh = new SingleMesh(vertexPositions, textureCoords, normals, indices);
        String name = mesh.mName().dataString();
        if (name.startsWith("@")) {
            name = name.replace("@", "");
            namedSingleMeshes.put(name, singleMesh);
        } else {
            otherSingleMeshes.add(singleMesh);
        }
    }

    public SingleMesh getSingleMesh(String name) {
        return namedSingleMeshes.get(name);
    }

    public List<SingleMesh> getOtherSingleMeshes() {
        return otherSingleMeshes;
    }

    private static final Map<String, NewModelLoader> modelLoaderMap = new HashMap<>();

    public static void init(String... modelFilePaths) {
        for (String path : modelFilePaths) {
            NewModelLoader loader = new NewModelLoader(path);
            modelLoaderMap.put(path, loader);
        }
    }

    public static NewModelLoader get(String path) {
        return modelLoaderMap.get(path);
    }

}
