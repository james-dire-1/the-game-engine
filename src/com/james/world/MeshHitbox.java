package com.james.world;

import com.james.math.Mth;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.Arrays;

public class MeshHitbox {

    public final PhysicalObject object;
    public final WallTriangle[] wallTriangles;

    public MeshHitbox(PhysicalObject object, TriangleMesh mesh) {
        this.object = object;
        this.wallTriangles = getWorldSpaceWallTriangles(mesh);
    }

    private WallTriangle[] getWorldSpaceWallTriangles(TriangleMesh mesh) {
        WallTriangle[] worldSpaceWallTriangles = new WallTriangle[mesh.wallTriangles.length];

        for (int i = 0; i < mesh.wallTriangles.length; i++) {
            WallTriangle localSpaceTriangle = mesh.wallTriangles[i];
            WallTriangle worldSpaceTriangle = getWorldSpaceTriangle(localSpaceTriangle);
            worldSpaceWallTriangles[i] = worldSpaceTriangle;
        }

        return worldSpaceWallTriangles;
    }

    private WallTriangle getWorldSpaceTriangle(Triangle localSpaceTriangle) {
        Matrix4f transformationMatrix = Mth.createTransformationMatrix(object.getPosition(), object.getRotation(), object.getScale());
        
        Vector4f localSpacePointA = new Vector4f(localSpaceTriangle.points[0].x, localSpaceTriangle.points[0].y, localSpaceTriangle.points[0].z, 1);
        Vector4f worldSpacePointA = Matrix4f.transform(transformationMatrix, localSpacePointA, null);
        Vector3f worldSpacePointAVec3 = new Vector3f(worldSpacePointA);

        Vector4f localSpacePointB = new Vector4f(localSpaceTriangle.points[1].x, localSpaceTriangle.points[1].y, localSpaceTriangle.points[1].z, 1);
        Vector4f worldSpacePointB = Matrix4f.transform(transformationMatrix, localSpacePointB, null);
        Vector3f worldSpacePointBVec3 = new Vector3f(worldSpacePointB);

        Vector4f localSpacePointC = new Vector4f(localSpaceTriangle.points[2].x, localSpaceTriangle.points[2].y, localSpaceTriangle.points[2].z, 1);
        Vector4f worldSpacePointC = Matrix4f.transform(transformationMatrix, localSpacePointC, null);
        Vector3f worldSpacePointCVec3 = new Vector3f(worldSpacePointC);

        Vector3f[] worldSpacePoints = {worldSpacePointAVec3, worldSpacePointBVec3, worldSpacePointCVec3};

        return new WallTriangle(worldSpacePoints);
    }

}
