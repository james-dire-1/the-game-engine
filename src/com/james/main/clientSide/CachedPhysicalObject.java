package com.james.main.clientSide;

import com.james.collisions.AbstractPhysicalObject;
import org.lwjgl.util.vector.Vector3f;

/**
 * Client side version of CachedPhysicalObject. Removes some things from the server PhysicalObject that
 * are not needed for the client.
 */
public class CachedPhysicalObject extends AbstractPhysicalObject {

    public final int id;

    protected final Vector3f rotation;
    protected float scale;

    public Vector3f getRotation() { return rotation; }
    public float getScale() { return scale; }

    public CachedPhysicalObject(int id, Vector3f position, Vector3f rotation, float scale) {
        super(position);
        this.id = id;

        this.rotation = rotation;
        this.scale = scale;
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
