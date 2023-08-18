package com.james.renderEngine.gameObjects;

import com.james.renderEngine.models.Model;
import org.lwjgl.util.vector.Vector3f;

/**
 * Notes: Position and rotation are made final because you shouldn't be reassigning their references!
 * Only the values at these references. This is why they are also private, to prevent any other class
 * from reassigning these variables' references. This is very important to ensure in order for it to
 * work nicely with other classes, like the SphereHitbox, whose position variable can just be the same
 * reference that its GameObject uses, so that way no updating is needed for the SphereHitbox if ever
 * the GameObject were to move.
 */
public class GameObject {

    public final Model model;
    private final Vector3f position;
    private final Vector3f rotation;
    private float scale;

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }
    public float getScale() { return scale; }

    public GameObject(Model model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void translate(Vector3f toTranslate) {
        position.x += toTranslate.x;
        position.y += toTranslate.y;
        position.z += toTranslate.z;
    }

    public void rotate(Vector3f toRotate) {
        rotation.x += toRotate.x;
        rotation.y += toRotate.y;
        rotation.z += toRotate.z;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
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
