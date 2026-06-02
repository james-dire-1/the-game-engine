package com.james.renderEngine.gameObjects;

import org.lwjgl.util.vector.Vector3f;

public class Light {

    public static final Vector3f DEFAULT_ATTENUATION = new Vector3f(1.0f, 0.01f, 0.002f);
    public static final Vector3f NO_ATTENUATION = new Vector3f(1, 0, 0);

    private final Vector3f position;
    private final Vector3f color;
    private final Vector3f attenuation;

    public Vector3f getPosition() { return position; }
    public Vector3f getColor() { return color; }
    public Vector3f getAttenuation() { return attenuation; }

    public Light(Vector3f position, Vector3f color) {
        this(position, color, NO_ATTENUATION);
    }

    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = new Vector3f(attenuation);
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

    public void setAttenuation(float att1, float att2, float att3) {
        this.attenuation.set(att1, att2, att3);
    }

}
