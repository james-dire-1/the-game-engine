package com.james.world;

import org.lwjgl.util.vector.Vector3f;

public class TriangleMesh {

    public final WallTriangle[] wallTriangles;

    public TriangleMesh(float[] vertexPositions, int[] indices) {
        // not divisible by three
        if (indices.length % 3 != 0) throw new RuntimeException();

        int triangleCount = indices.length / 3;
        wallTriangles = new WallTriangle[triangleCount];

        int count = 0;
        for (int i = 0; i < indices.length; i += 3) {
            int indexPointA = indices[i];
            int indexPointB = indices[i + 1];
            int indexPointC = indices[i + 2];

            float pointAXCoord = vertexPositions[indexPointA*3];
            float pointAYCoord = vertexPositions[indexPointA*3 + 1];
            float pointAZCoord = vertexPositions[indexPointA*3 + 2];
            Vector3f pointA = new Vector3f(pointAXCoord, pointAYCoord, pointAZCoord);

            float pointBXCoord = vertexPositions[indexPointB*3];
            float pointBYCoord = vertexPositions[indexPointB*3 + 1];
            float pointBZCoord = vertexPositions[indexPointB*3 + 2];
            Vector3f pointB = new Vector3f(pointBXCoord, pointBYCoord, pointBZCoord);

            float pointCXCoord = vertexPositions[indexPointC*3];
            float pointCYCoord = vertexPositions[indexPointC*3 + 1];
            float pointCZCoord = vertexPositions[indexPointC*3 + 2];
            Vector3f pointC = new Vector3f(pointCXCoord, pointCYCoord, pointCZCoord);

            WallTriangle wallTriangle =  new WallTriangle(new Vector3f[]{pointA, pointB, pointC});
            wallTriangles[count] = wallTriangle;

            count++;
        }
    }

}
