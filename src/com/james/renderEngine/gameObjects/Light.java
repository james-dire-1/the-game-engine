package com.james.renderEngine.gameObjects;

import org.lwjgl.util.vector.Vector3f;

public class Light {

    private final Vector3f position;
    private final Vector3f color;

    public Vector3f getPosition() { return position; }
    public Vector3f getColor() { return color; }

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public void translate(Vector3f toTranslate) {
        position.x += toTranslate.x;
        position.y += toTranslate.y;
        position.z += toTranslate.z;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

}
