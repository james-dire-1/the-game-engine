package com.james.collisions;

import com.james.main.clientSide.OldClientCollisionHandler;
import com.james.math.Mth;
import com.james.world.OldCollisionHandler;
import com.james.world.WallTriangle;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Utility class used by both the ClientCollisionHandler and the server CollisionHandler.
 * @see OldClientCollisionHandler
 * @see OldCollisionHandler
 */
public class OldCollisionMath {

    public static void collisionBetweenObjectAndWallTriangle(AbstractObjectHitbox objectHitbox, WallTriangle triangle) {
        Vector2f a = null;
        Vector2f b = null;
        Vector2f c = null;
        Vector2f p = null;

        Vector3f playerPosition = objectHitbox.object.getPosition();
        if (triangle.mode == WallTriangle.ExtendingMode.Vertical) {
            a = new Vector2f(triangle.points[0].x, triangle.points[0].y);
            b = new Vector2f(triangle.points[1].x, triangle.points[1].y);
            c = new Vector2f(triangle.points[2].x, triangle.points[2].y);
            p = new Vector2f(playerPosition.x, playerPosition.y);
        } else if (triangle.mode == WallTriangle.ExtendingMode.Horizontal) {
            a = new Vector2f(triangle.points[0].x, triangle.points[0].z);
            b = new Vector2f(triangle.points[1].x, triangle.points[1].z);
            c = new Vector2f(triangle.points[2].x, triangle.points[2].z);
            p = new Vector2f(playerPosition.x, playerPosition.z);
        }

        // TODO: 2023-08-16 Optimize this code in the future so that t isn't negative in front of the wall
        // TODO: 2023-08-16 and so that we can reverse the conditionals in the if statement
        if (Mth.pointInTriangle(a, b, c, p)) {
            float t = Vector3f.dot(triangle.unitVector, triangle.points[0]) - Vector3f.dot(triangle.unitVector, objectHitbox.object.getPosition());

            if (t >= -objectHitbox.radius && t <= 0) {
                float distanceFromEdge = objectHitbox.radius + t;
                Vector3f newPositionOfObject = Vector3f.add(objectHitbox.object.getPosition(), Mth.multiply(triangle.unitVector, distanceFromEdge), null);
                objectHitbox.object.setPosition(newPositionOfObject.x, newPositionOfObject.y, newPositionOfObject.z);
            }
        }
    }

}
