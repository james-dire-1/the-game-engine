package com.james.evenNewerCollisionsStuff;

import com.james.world.Triangle;
import org.lwjgl.util.vector.Vector3f;

// TODO: 2024-06-16 Say where you got this from
public class Plane {

    public final float[] equation = new float[4];
    public final Vector3f origin;
    public final Vector3f normal;

    public Plane(Vector3f origin, Vector3f normal) {
        this.origin = origin;
        this.normal = normal;

        setEquation();
    }

    public Plane(Vector3f p1, Vector3f p2, Vector3f p3) {
        this.origin = p1;
        this.normal = Vector3f.cross(Vector3f.sub(p2, p1, null), Vector3f.sub(p3, p1, null), null);
        this.normal.normalise(this.normal);

        setEquation();
    }

    private void setEquation() {
        this.equation[0] = normal.x;
        this.equation[1] = normal.y;
        this.equation[2] = normal.z;
        this.equation[3] = -(normal.x*origin.x + normal.y*origin.y + normal.z*origin.z);
    }

    public boolean isFrontFacingTo(Vector3f direction) {
        double dot = Vector3f.dot(normal, direction);
        return dot <= 0;
    }

    public double signedDistanceTo(Vector3f point) {
        return Vector3f.dot(point, normal) + equation[3];
    }

}
