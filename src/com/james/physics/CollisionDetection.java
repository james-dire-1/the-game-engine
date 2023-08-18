package com.james.physics;

import com.james.renderEngine.gameObjects.GameObject;
import com.james.math.Mth;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class CollisionDetection {

    public static boolean isColliding(SphereHitbox a, SphereHitbox b) {
        float distance = Mth.distance(a.position, b.position);
        return distance < a.radius + b.radius;
    }

    public static boolean isColliding(SphereHitbox a, GameObject b) {
        Matrix4f transformationMatrix = Mth.createTransformationMatrix(b.getPosition(), b.getRotation(), b.getScale());
        float[] vertexPositions = b.model.rawModel.vertexPositions;
        for (int i = 0; i < vertexPositions.length; i += 3) {
            float xCoordInLocal = vertexPositions[i];
            float yCoordInLocal = vertexPositions[i + 1];
            float zCoordInLocal = vertexPositions[i + 2];
            Vector4f vertexPositionInLocal = new Vector4f(xCoordInLocal, yCoordInLocal, zCoordInLocal, 1);
            Vector4f vertexPositionInWorld = Matrix4f.transform(transformationMatrix, new Vector4f(vertexPositionInLocal), null);
            Vector3f vertexPositionInWorldVec3 = new Vector3f(vertexPositionInWorld.x, vertexPositionInWorld.y, vertexPositionInWorld.z);
            float distance = Mth.distance(a.position, vertexPositionInWorldVec3);
            if (distance < a.radius) {
                return true;
            }
        }

        return false;
    }

}
