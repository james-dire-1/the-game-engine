package com.james.evenNewerCollisionsStuff;

import org.lwjgl.util.vector.Vector3f;

public class PointOperations {

    // TODO: 2024-06-16 Make a more efficient version of this method that does not return a new Vector3f instance
    public static Vector3f dividePointByEllipsoidRadius(Vector3f pointInR3, Vector3f ellipsoidRadius) {
        float ellipsoidSpaceX = pointInR3.x / ellipsoidRadius.x;
        float ellipsoidSpaceY = pointInR3.y / ellipsoidRadius.y;
        float ellipsoidSpaceZ = pointInR3.z / ellipsoidRadius.z;

        return new Vector3f(ellipsoidSpaceX, ellipsoidSpaceY, ellipsoidSpaceZ);
    }

    // TODO: 2024-06-18 Same as what was mentioned above
    public static Vector3f multiplyPointByEllipsoidRadius(Vector3f pointInEllipsoidSpace, Vector3f ellipsoidRadius) {
        float r3X = pointInEllipsoidSpace.x * ellipsoidRadius.x;
        float r3Y = pointInEllipsoidSpace.y * ellipsoidRadius.y;
        float r3Z = pointInEllipsoidSpace.z * ellipsoidRadius.z;

        return new Vector3f(r3X, r3Y, r3Z);
    }

    public static Vector3f addObjectPositionToPoint(Vector3f pointInEllipsoidLocalSpace, Vector3f objectPosition) {
        float ellipsoidWorldSpaceX = pointInEllipsoidLocalSpace.x + objectPosition.x;
        float ellipsoidWorldSpaceY = pointInEllipsoidLocalSpace.y + objectPosition.y;
        float ellipsoidWorldSpaceZ = pointInEllipsoidLocalSpace.z + objectPosition.z;

        return new Vector3f(ellipsoidWorldSpaceX, ellipsoidWorldSpaceY, ellipsoidWorldSpaceZ);
    }

}
