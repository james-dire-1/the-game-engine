package com.james.simulation.objects;

import com.james.renderEngine.gameObjects.GameObject;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a player object on the client-side.
 */
public class CachedConnectedPlayer {

    private GameObject gameObject;
    public GameObject getGameObject() { return gameObject; }

    private final Vector3f position;
    private final Vector3f rotation;

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }

    public CachedConnectedPlayer(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
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

}
