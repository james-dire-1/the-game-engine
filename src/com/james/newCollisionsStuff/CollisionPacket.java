package com.james.newCollisionsStuff;

import org.lwjgl.util.vector.Vector3f;

public class CollisionPacket {

    public Vector3f ellipsoidRadius;

    public Vector3f r3Velocity;
    public Vector3f r3Position;

    public Vector3f eVelocity;
    public Vector3f eNormalizedVelocity;
    public Vector3f eBasePoint;

    public boolean foundCollision;
    public double nearestDistance;
    public Vector3f intersectionPoint;

}
