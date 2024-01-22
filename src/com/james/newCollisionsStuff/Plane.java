package com.james.newCollisionsStuff;

import org.lwjgl.util.vector.Vector3f;

// Note: implemented from Fauerby's report:
// http://www.peroxide.dk/papers/collision/collision.pdf
public class Plane {

    public float[] equation = new float[4];
    public Vector3f origin;
    public Vector3f normal;

    public Plane(Vector3f origin, Vector3f normal) {
        this.origin = origin;
        this.normal = normal;

        equation[0] = normal.x;
        equation[1] = normal.y;
        equation[2] = normal.z;
        equation[3] = -(normal.x*origin.x + normal.y*origin.y + normal.z*origin.z);
    }

    // TODO: 2024-01-16 This is duplicate code in the triangle class. Consider combining it?
    public Plane(Vector3f p0, Vector3f p1, Vector3f p2) {
        this.origin = p0;

        Vector3f v1 = Vector3f.sub(p1, p0, null);
        Vector3f v2 = Vector3f.sub(p2, p0, null);

        this.normal = Vector3f.cross(v1, v2, null);
        this.normal.normalise(this.normal);

        equation[0] = normal.x;
        equation[1] = normal.y;
        equation[2] = normal.z;
        equation[3] = -(normal.x*origin.x + normal.y*origin.y + normal.z*origin.z);
    }

    public boolean isFrontFacingTo(Vector3f direction) {
        float dotProduct = Vector3f.dot(normal, direction);
        return (dotProduct <= 0);
    }

    public float signedDistanceTo(Vector3f point) {
        return Vector3f.dot(point, normal) + equation[3];
    }

}
