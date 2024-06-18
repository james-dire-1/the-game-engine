package com.james.evenNewerCollisionsStuff;

import com.james.world.Triangle;
import org.lwjgl.util.vector.Vector3f;

public class ModelMesh {

    public Triangle[] triangles;
    public float minX, maxX, minY, maxY, minZ, maxZ;

    public ModelMesh(float[] vertexPositions, int[] indices) {
        // not divisible by three
        if (indices.length % 3 != 0) throw new RuntimeException();

        int triangleCount = indices.length / 3;
        triangles = new Triangle[triangleCount];

        minX = Float.MAX_VALUE;
        minY = Float.MAX_VALUE;
        minZ = Float.MAX_VALUE;

        maxX = Float.MIN_VALUE;
        maxY = Float.MIN_VALUE;
        maxZ = Float.MIN_VALUE;

        int count = 0;
        for (int i = 0; i < indices.length; i += 3) {
            int indexPointA = indices[i];
            int indexPointB = indices[i + 1];
            int indexPointC = indices[i + 2];

            Vector3f pointA = processPoint(indexPointA, vertexPositions);
            Vector3f pointB = processPoint(indexPointB, vertexPositions);
            Vector3f pointC = processPoint(indexPointC, vertexPositions);

            Triangle triangle = new Triangle(new Vector3f[]{pointA, pointB, pointC});
            triangles[count] = triangle;

            count++;
        }
    }

    public ModelMesh(Triangle[] triangles) {
        this.triangles = triangles;
    }

    private Vector3f processPoint(int indexForPoint, float[] vertexPositions) {
        float xCoord = vertexPositions[indexForPoint*3];
        float yCoord = vertexPositions[indexForPoint*3 + 1];
        float zCoord = vertexPositions[indexForPoint*3 + 2];

        if (xCoord < minX) minX = xCoord;
        if (xCoord > maxX) maxX = xCoord;
        if (yCoord < minY) minY = yCoord;
        if (yCoord > maxY) maxY = yCoord;
        if (zCoord < minZ) minZ = zCoord;
        if (zCoord > maxZ) maxZ = zCoord;

        return new Vector3f(xCoord, yCoord, zCoord);
    }

    public static Triangle[] performOperationOnAllTriangles(Triangle[] inputTriangles, Operation operation, Vector3f other) {
        Triangle[] outputTriangles = new Triangle[inputTriangles.length];

        for (int i = 0; i < inputTriangles.length; i++) {
            Vector3f[] inputPoints = inputTriangles[i].points;
            Vector3f[] outputPoints = new Vector3f[3];

            for (int j = 0; j <= 2; j++) {
                Vector3f inputPoint = inputPoints[j];
                Vector3f outputPoint = operation.perform(inputPoint, other);

                outputPoints[j] = outputPoint;
            }

            outputTriangles[i] = new Triangle(outputPoints);
        }

        return outputTriangles;
    }

    public interface Operation {
        Vector3f perform(Vector3f inputPoint, Vector3f other);
    }

}
