package com.james.world;

import org.lwjgl.util.vector.Vector3f;

/**
 * Triangle class used as the most basic unit for a mesh. Includes its 3 points that define it,
 * as well as its unit vector. These can be either in local space (respective to origin), or in
 * world space (respective to the world).
 */
public class Triangle {
    // TODO: 2024-06-16 Do we need a new Triangle class?

    public final Vector3f[] points;
    public final Vector3f unitVector;

    /**
     * Constructs a new Triangle.
     * @implNote In the constructor is the calculations necessary for obtaining this triangle's unit vector.
     */
    public Triangle(Vector3f[] points) {
        if (points.length != 3) throw new RuntimeException();

        this.points = points;

        Vector3f v1 = Vector3f.sub(points[1], points[0], null);
        Vector3f v2 = Vector3f.sub(points[2], points[0], null);

        this.unitVector = Vector3f.cross(v1, v2, null);
        this.unitVector.normalise(this.unitVector);
    }

    /**
     * Constructor to be used client-side.
     */
    public Triangle(Vector3f[] points, Vector3f unitVector) {
        this.points = points;
        this.unitVector = unitVector;
    }

    @Override
    public String toString() {
        return points[0].toString() + " | " + points[1].toString() + " | " + points[2].toString();
    }

}
