package com.james.tools;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Used ThinMatrix video
public class OBJParser {

    public static ModelData parseOBJFile(String name) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(OBJParser.class.getResourceAsStream(name)));
        String line;
        List<Vector3f> vertexPositions = new ArrayList<>();
        List<Vector2f> textureCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] vertexPositionsArray = null;
        float[] textureCoordsArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertexPositions.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textureCoords.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureCoordsArray = new float[vertexPositions.size() * 2];
                    normalsArray = new float[vertexPositions.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textureCoords, normals, textureCoordsArray, normalsArray);
                processVertex(vertex2, indices, textureCoords, normals, textureCoordsArray, normalsArray);
                processVertex(vertex3, indices, textureCoords, normals, textureCoordsArray, normalsArray);
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        vertexPositionsArray = new float[vertexPositions.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertexPositions) {
            vertexPositionsArray[vertexPointer++] = vertex.x;
            vertexPositionsArray[vertexPointer++] = vertex.y;
            vertexPositionsArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return new ModelData(vertexPositionsArray, indicesArray, textureCoordsArray, normalsArray);

    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textureCoords,
                                      List<Vector3f> normals, float[] textureCoordsArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textureCoords.get(Integer.parseInt(vertexData[1]) - 1);
        textureCoordsArray[currentVertexPointer * 2] = currentTex.x;
        textureCoordsArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

    public static class ModelData {
        public final float[] vertexPositions;
        public final float[] textureCoords;
        public final float[] normals;
        public final int[] indices;

        public ModelData(float[] vertexPositions, int[] indices, float[] textureCoords, float[] normals) {
            this.vertexPositions = vertexPositions;
            this.textureCoords = textureCoords;
            this.normals = normals;
            this.indices = indices;
        }
    }

}
