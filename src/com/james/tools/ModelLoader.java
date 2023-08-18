package com.james.tools;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

// TODO: 2023-01-04 Specify where you got this info from
public class ModelLoader {

    private final List<Float> vertexPositions = new ArrayList<>();
    private final List<Float> textureCoords = new ArrayList<>();
    private final List<Float> normals = new ArrayList<>();
    private final List<Integer> indices = new ArrayList<>();

    public float[] vertexPositions() {
        float[] vertexPositionsArray = new float[vertexPositions.size()];
        for (int i = 0; i < vertexPositions.size(); i++) {
            vertexPositionsArray[i] = vertexPositions.get(i);
        }
        return vertexPositionsArray;
    }

    public float[] textureCoords() {
        float[] textureCoordsArray = new float[textureCoords.size()];
        for (int i = 0; i < textureCoords.size(); i++) {
            textureCoordsArray[i] = textureCoords.get(i);
        }
        return textureCoordsArray;
    }

    public float[] normals() {
        float[] normalsArray = new float[normals.size()];
        for (int i = 0; i < normals.size(); i++) {
            normalsArray[i] = normals.get(i);
        }
        return normalsArray;
    }

    public int[] indices() {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    public ModelLoader(String path) {
        AIScene scene = aiImportFile(path, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_JoinIdenticalVertices);
//        AIScene scene = aiImportFileEx(path, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_JoinIdenticalVertices, AIFileIO.create());

        if (scene == null) {
            System.err.println("Assimp import failed! " + aiGetErrorString());
        }
        
        PointerBuffer buffer = scene.mMeshes();

        for (int i = 0; i < buffer.limit(); i++) {
            AIMesh mesh = AIMesh.create(buffer.get(i));
            processMesh(mesh);
        }
    }

    private void processMesh(AIMesh mesh) {
        mesh.mFaces();

        AIVector3D.Buffer vectors = mesh.mVertices();

        for (int i = 0; i < vectors.limit(); i++) {
            AIVector3D vector = vectors.get(i);

            vertexPositions.add(vector.x());
            vertexPositions.add(vector.y());
            vertexPositions.add(vector.z());
        }

        AIVector3D.Buffer coords = mesh.mTextureCoords(0);

        for (int i = 0; i < coords.limit(); i++) {
            AIVector3D coord = coords.get(i);

            textureCoords.add(coord.x());
            textureCoords.add(coord.y());
        }

        AIVector3D.Buffer norms = mesh.mNormals();

        for (int i = 0; i < norms.limit(); i++) {
            AIVector3D norm = norms.get(i);

            normals.add(norm.x());
            normals.add(norm.y());
            normals.add(norm.z());
        }

        AIFace.Buffer faces = mesh.mFaces();

        for (int i = 0; i < faces.limit(); i++) {
            AIFace face = faces.get(i);
            IntBuffer indicesForFace = face.mIndices();
            indices.add(indicesForFace.get(0));
            indices.add(indicesForFace.get(1));
            indices.add(indicesForFace.get(2));
        }
    }

}
