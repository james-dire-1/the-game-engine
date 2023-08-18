package com.james.world;

import com.james.collisions.AbstractPhysicalObject;
import org.lwjgl.util.vector.Vector3f;

public class PhysicalObject extends AbstractPhysicalObject {

    public final int id;
    public final PhysicalObjectType type;

    protected final Vector3f rotation;
    protected float scale;

    public Vector3f getRotation() { return rotation; }
    public float getScale() { return scale; }

    private boolean shouldDelete = false;

    private static int count;

    public PhysicalObject(Vector3f position, Vector3f rotation, float scale) {
        this(PhysicalObjectType.Other, position, rotation, scale);
    }

    public PhysicalObject(PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        super(position);
        this.type = type;
        this.rotation = rotation;
        this.scale = scale;

        count++;
        this.id = count;
    }

    public boolean update() {
        return shouldDelete;
    }

    protected void markForDeletion() {
        shouldDelete = true;
    }

    public void rotate(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
