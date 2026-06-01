package com.james.renderEngine.gameObjects;

import org.lwjgl.util.vector.Vector3f;

public class DirectionalLight {

    private final Vector3f toLightDirection;
    private final Vector3f color;

    public Vector3f getToLightDirection() { return toLightDirection; }
    public Vector3f getColor() { return color; }

    public DirectionalLight(Vector3f toLightDirection, Vector3f color) {
        this.toLightDirection = toLightDirection;
        this.color = color;
    }

    public void setToLightDirection(float x, float y, float z) {
        this.toLightDirection.set(x, y, z);
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

}
