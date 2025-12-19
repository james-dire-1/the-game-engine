package com.james.common.tools;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import templates.common.GlobalConstants;

import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.assimp.Assimp.*;

// https://www.youtube.com/watch?v=eqlwamit0vU&t=883s 
public class ModelLoader {

    public float[] vertexPositions;
    public float[] textureCoords;
    public float[] normals;
    public int[] indices;

    private ModelLoader(String path) {
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

        List<Float> vertexPositionsList = new ArrayList<>();
        List<Float> textureCoordsList = new ArrayList<>();
        List<Float> normalsList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        for (int i = 0; i < buffer.limit(); i++) {
            AIMesh mesh = AIMesh.create(buffer.get(i));
            processMesh(mesh, vertexPositionsList, textureCoordsList, normalsList, indicesList, path, i);

            if (path.equals("/blender-test-7.dae")) {
                System.out.println("i = " + i);
                System.out.println("vertexPositions.length = " + vertexPositionsList.size()/3);
                System.out.println("textureCoords.length = " + textureCoordsList.size()/2);
                System.out.println("normals.length = " + normalsList.size()/3);
                System.out.println("indices.length = " + indicesList.size());
                System.out.println("----------");
            }
        }

        vertexPositions = new float[vertexPositionsList.size()];
        for (int i = 0; i < vertexPositionsList.size(); i++) {
            vertexPositions[i] = vertexPositionsList.get(i);
        }

        textureCoords = new float[textureCoordsList.size()];
        for (int i = 0; i < textureCoordsList.size(); i++) {
            textureCoords[i] = textureCoordsList.get(i);
        }

        normals = new float[normalsList.size()];
        for (int i = 0; i < normalsList.size(); i++) {
            normals[i] = normalsList.get(i);
        }

        indices = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }

//        if (path.equals("/blender-test-7.dae")) {
//            System.out.println("vertexPositions.length = " + vertexPositions.length/3);
//            System.out.println("textureCoords.length = " + textureCoords.length/2);
//            System.out.println("normals.length = " + normals.length/3);
//            System.out.println("indices.length = " + indices.length);
//        }
    }

    private void processMesh(AIMesh mesh, List<Float> vertexPositionsList, List<Float> textureCoordsList,
                             List<Float> normalsList, List<Integer> indicesList, String path, int count) {
        // Vertices
        AIVector3D.Buffer vectors = mesh.mVertices();
        Objects.requireNonNull(vectors);

        for (int i = 0; i < vectors.limit(); i++) {
            AIVector3D vector = vectors.get(i);

            vertexPositionsList.add(vector.x());
            vertexPositionsList.add(vector.y());
            vertexPositionsList.add(vector.z());

//            if (path.equals("/blender-test-7.dae")) {
//                System.out.println(vector.x() + ", " + vector.y() + ", " + vector.z());
//            }
        }

        // Texture Coords
        AIVector3D.Buffer coords = mesh.mTextureCoords(0);
        Objects.requireNonNull(coords);

        for (int i = 0; i < coords.limit(); i++) {
            AIVector3D coord = coords.get(i);

            textureCoordsList.add(coord.x());
            textureCoordsList.add(coord.y());
        }

        // Normals
        AIVector3D.Buffer norms = mesh.mNormals();
        Objects.requireNonNull(norms);

        for (int i = 0; i < norms.limit(); i++) {
            AIVector3D norm = norms.get(i);

            normalsList.add(norm.x());
            normalsList.add(norm.y());
            normalsList.add(norm.z());
        }

        // Indices
        AIFace.Buffer faces = mesh.mFaces();

        for (int i = 0; i < faces.limit(); i++) {
            AIFace face = faces.get(i);
            IntBuffer indicesForFace = face.mIndices();
            indicesList.add(indicesForFace.get(0) + count * 24);
            indicesList.add(indicesForFace.get(1) + count * 24);
            indicesList.add(indicesForFace.get(2) + count * 24);

            if (path.equals("/blender-test-7.dae")) {
                System.out.println(indicesForFace.get(0) + ", " + indicesForFace.get(1) + ", " + indicesForFace.get(2));
            }
        }
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
