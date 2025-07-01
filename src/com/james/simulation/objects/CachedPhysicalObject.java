package com.james.simulation.objects;

import com.james.common.simulation.objects.AbstractPhysicalObject;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

/**
 * Client side version of CachedPhysicalObject. Removes some things from the server PhysicalObject that
 * are not needed for the client, and adds prevPosition, which is necessary for interpolation.
 */
public class CachedPhysicalObject extends AbstractPhysicalObject {

    public float lastTime = Time.getCurrentTime();

    private GameObject gameObject;
    public GameObject getGameObject() { return gameObject; }

    protected final Vector3f rotation;
    protected float scale;
    protected final Vector3f prevPosition;
    protected final Vector3f prevRotation;

    public Vector3f getRotation() { return rotation; }
    public float getScale() { return scale; }
    public Vector3f getPrevPosition() { return prevPosition; }
    public Vector3f getPrevRotation() { return prevRotation; }

    public CachedPhysicalObject(Vector3f position, Vector3f rotation, float scale) {
        super(position);

        this.rotation = rotation;
        this.scale = scale;
        this.prevPosition = new Vector3f(position);
        this.prevRotation = new Vector3f(rotation);
    }

    // TODO: 2025-07-01 This should only be done if the object moves 
    public void updatePrevPosition() {
        this.prevPosition.set(this.position);
    }

    public void updatePrevRotation() {
        this.prevRotation.set(this.rotation);
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
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
