package com.james.simulation.objects;

import com.james.common.simulation.objects.AbstractPhysicalObject;
import com.james.renderEngine.gameObjects.GameObject;
import org.lwjgl.util.vector.Vector3f;

/**
 * Client side version of CachedPhysicalObject. Removes some things from the server PhysicalObject that
 * are not needed for the client, and adds prevPosition, which is necessary for interpolation.
 */
public class CachedPhysicalObject extends AbstractPhysicalObject {

    private GameObject gameObject;
    public GameObject getGameObject() { return gameObject; }

    protected final Vector3f prevPosition;
    protected final Vector3f rotation;
    protected float scale;

    public Vector3f getPrevPosition() { return prevPosition; }
    public Vector3f getRotation() { return rotation; }
    public float getScale() { return scale; }

    public CachedPhysicalObject(Vector3f position, Vector3f rotation, float scale) {
        super(position);

        this.prevPosition = new Vector3f(position);
        this.rotation = rotation;
        this.scale = scale;
    }

    // TODO: 2025-07-01 This should only be done if the object moves 
    public void updatePrevPosition() {
        this.prevPosition.x = this.position.x;
        this.prevPosition.y = this.position.y;
        this.prevPosition.z = this.position.z;
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
