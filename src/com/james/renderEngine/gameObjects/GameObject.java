package com.james.renderEngine.gameObjects;

import com.james.renderEngine.models.Model;
import com.james.simulation.objects.CachedPhysicalObject;
import templates.communication.ClientPacketReceiveActions;
import org.lwjgl.util.vector.Vector3f;

/**
 * Representation of an object that can be rendered on the client side. However, there is no simulation
 * handled here.
 * @implNote Position and rotation are made final because you shouldn't be reassigning their references!
 * Only the values at these references.
 * One side effect of this is that the programmer can just use references from CachedPhysicalObjects as
 * references in this class, and thus when CachedPhysicalObjects are moved, GameObjects are moved
 * automatically. This behaviour can be seen in ClientPacketReceiveActions. However, this technique can't
 * be used for smooth interpolating.
 * @see CachedPhysicalObject
 * @see ClientPacketReceiveActions
 */
public class GameObject {

    public final Model model;
    private final Vector3f position;
    private final Vector3f rotation;
    private float scale;

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }
    public float getScale() { return scale; }

    public boolean isVisible = true;
    public boolean isAffectedByFog = true;
    public float highlightFactor = 0.0f;

    public boolean deleted = false;

    public GameObject(Model model, Vector3f position) {
        this(model, position, new Vector3f(), 1);
    }

    public GameObject(Model model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
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
