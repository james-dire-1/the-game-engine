package com.james.simulation.objects;

import com.james.renderEngine.gameObjects.GameObject;
import com.james.tools.ColorUtils;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a player object on the client-side.
 */
public class CachedConnectedPlayer {

    public final String username;
    public final float[] color;

    public float lastTime = Time.getCurrentTime();

    private GameObject gameObject;
    public GameObject getGameObject() { return gameObject; }

    private final Vector3f position;
    private final Vector3f rotation;
    private final Vector3f prevPosition;
    private final Vector3f prevRotation;

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }
    public Vector3f getPrevPosition() { return prevPosition; }
    public Vector3f getPrevRotation() { return prevRotation; }

    public CachedConnectedPlayer(String username, int color, Vector3f position, Vector3f rotation) {
        this.username = username;
        this.color = ColorUtils.asNormalizedRGBArray(color);

        this.position = position;
        this.rotation = rotation;
        this.prevPosition = new Vector3f(position);
        this.prevRotation = new Vector3f(rotation);
    }

    public void updatePrevPosition() {
        this.prevPosition.set(this.position);
    }

    public void updatePrevRotation() {
        this.prevRotation.set(this.rotation);
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
