package com.james.physics;

import org.lwjgl.util.vector.Vector3f;

public class SphereHitbox {

    public Vector3f position;
    public float radius;

    public SphereHitbox(Vector3f position, float radius) {
        this.position = position;
        this.radius = radius;
    }

}
