package com.james.renderEngine.gameObjects;

import org.lwjgl.util.vector.Vector3f;

public class Light {

    public Vector3f position;
    public Vector3f color;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public void translate(Vector3f toTranslate) {
        position.x += toTranslate.x;
        position.y += toTranslate.y;
        position.z += toTranslate.z;
    }

}
