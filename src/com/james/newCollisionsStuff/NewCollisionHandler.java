package com.james.newCollisionsStuff;

import com.james.math.Mth;
import org.lwjgl.util.vector.Vector3f;

public class NewCollisionHandler {

    // TODO: 2024-01-16 Consider changing this to check to accept your custom triangle class instead of the three points
    public static void checkTriangle(CollisionPacket collisionPacket, Vector3f p0, Vector3f p1, Vector3f p2) {
        Plane trianglePlane = new Plane(p0, p1, p2);

        if (trianglePlane.isFrontFacingTo(collisionPacket.eNormalizedVelocity));

        float t0, t1;
        boolean embeddedInPlane = false;

        float signedDistanceToTrianglePlane = trianglePlane.signedDistanceTo(collisionPacket.eBasePoint);

        float normalDotVelocity = Vector3f.dot(trianglePlane.normal, collisionPacket.eVelocity);

        if (normalDotVelocity == 0.0f) {
            if (Math.abs(signedDistanceToTrianglePlane) >= 1.0f) {
                return;
            } else {
                embeddedInPlane = true;
                t0 = 0.0f;
                t1 = 1.0f;
            }
        } else {
            t0 = (-1 - signedDistanceToTrianglePlane) / normalDotVelocity;
            t1 = (1 - signedDistanceToTrianglePlane) / normalDotVelocity;

            if (t0 > t1) {
                float temp = t1;
                t1 = t0;
                t0 = temp;
            }

            if (t0 > 1.0 || t1 < 0.0) {
                return;
            }

            if (t0 < 0.0) t0 = 0.0f;
            if (t1 < 0.0) t1 = 0.0f;
            if (t0 > 1.0) t0 = 1.0f;
            if (t1 > 1.0) t1 = 1.0f;

            Vector3f collisionPoint;
            boolean foundCollision = false;
            float t = 1.0f;

            if (!embeddedInPlane) {
                Vector3f leftSide = Vector3f.sub(collisionPacket.eBasePoint, trianglePlane.normal, null);
                Vector3f rightSide = Mth.multiply(collisionPacket.eVelocity, t0);
                Vector3f planeIntersectionPoint = Vector3f.add(leftSide, rightSide, null);

                if (Mth.pointInTriangle(p0, p1, p2, planeIntersectionPoint)) {

                }
            }
        }
    }

}
