package com.james.physics;

import org.lwjgl.util.vector.Vector3f;

// TODO: 2024-06-03 Consider removing this class? Doesn't seem to be used anymore, and is outdated
public class SphereHitbox {

    public Vector3f position;
    public float radius;

    public SphereHitbox(Vector3f position, float radius) {
        this.position = position;
        this.radius = radius;
    }

}
