package newStuff.evenNewer;

import java.util.List;

public class ModelLoaderHelper {

    public static float[] vertexPositionsAllMeshes(ModelLoader loader) {
        List<SingleMesh> singleMeshes = loader.getAllSingleMeshes();

        int arrayLength = 0;
        for (SingleMesh singleMesh : singleMeshes) {
            arrayLength += singleMesh.vertexPositions.length;
        }

        float[] vertexPositionsAllMeshes = new float[arrayLength];
        int currentIndex = 0;

        for (SingleMesh singleMesh : singleMeshes) {
            for (float vertexCoord : singleMesh.vertexPositions) {
                vertexPositionsAllMeshes[currentIndex] = vertexCoord;
                currentIndex++;
            }
        }

        return vertexPositionsAllMeshes;
    }

    public static float[] textureCoordsAllMeshes(ModelLoader loader) {
        List<SingleMesh> singleMeshes = loader.getAllSingleMeshes();

        int arrayLength = 0;
        for (SingleMesh singleMesh : singleMeshes) {
            arrayLength += singleMesh.textureCoords.length;
        }

        float[] textureCoordsAllMeshes = new float[arrayLength];
        int currentIndex = 0;

        for (SingleMesh singleMesh : singleMeshes) {
            for (float textureCoord : singleMesh.textureCoords) {
                textureCoordsAllMeshes[currentIndex] = textureCoord;
                currentIndex++;
            }
        }

        return textureCoordsAllMeshes;
    }

    public static float[] normalsAllMeshes(ModelLoader loader) {
        List<SingleMesh> singleMeshes = loader.getAllSingleMeshes();

        int arrayLength = 0;
        for (SingleMesh singleMesh : singleMeshes) {
            arrayLength += singleMesh.normals.length;
        }

        float[] normalsAllMeshes = new float[arrayLength];
        int currentIndex = 0;

        for (SingleMesh singleMesh : singleMeshes) {
            for (float normalCoord : singleMesh.normals) {
                normalsAllMeshes[currentIndex] = normalCoord;
                currentIndex++;
            }
        }

        return normalsAllMeshes;
    }

    public static int[] indicesAllMeshes(ModelLoader loader) {
        List<SingleMesh> singleMeshes = loader.getAllSingleMeshes();

        int arrayLength = 0;
        for (SingleMesh singleMesh : singleMeshes) {
            arrayLength += singleMesh.indices.length;
        }

        int[] indicesAllMeshes = new int[arrayLength];
        int glIndexOffset = 0;
        int currentIndex = 0;

        for (SingleMesh singleMesh : singleMeshes) {
            for (int glIndex : singleMesh.indices) {
                indicesAllMeshes[currentIndex] = glIndexOffset + glIndex;
                currentIndex++;
            }

            if (singleMesh.vertexPositions.length % 3 != 0)
                throw new RuntimeException();

            glIndexOffset += singleMesh.vertexPositions.length / 3;
        }

        return indicesAllMeshes;
    }

}
