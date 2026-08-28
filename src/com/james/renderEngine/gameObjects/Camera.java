package com.james.renderEngine.gameObjects;

import com.james.tools.RenderingMath;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;
    private float secondaryPitch;
    private Matrix4f viewMatrix;
    private boolean hasCameraMoved = false;

    public Vector3f getPosition() { return position; }
    public float getPitch() { return pitch; }
    public float getYaw() { return yaw; }
    public float getRoll() { return roll; }
    public float getSecondaryPitch() { return secondaryPitch; }

    public Camera(Vector3f position, float pitch, float yaw, float roll) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.viewMatrix = RenderingMath.createViewMatrix(position, pitch, yaw, roll);
    }

    public void translate(Vector3f toTranslate) {
        position.x += toTranslate.x;
        position.y += toTranslate.y;
        position.z += toTranslate.z;
        hasCameraMoved = true;
    }

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
        hasCameraMoved = true;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        hasCameraMoved = true;
    }

    public void increasePitch(float dPitch) {
        this.pitch += dPitch;
        hasCameraMoved = true;
    }

    public void increaseYaw(float dYaw) {
        this.yaw += dYaw;
        hasCameraMoved = true;
    }

    public void increaseRoll(float dRoll) {
        this.roll += dRoll;
        hasCameraMoved = true;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        hasCameraMoved = true;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        hasCameraMoved = true;
    }

    public void setRoll(float roll) {
        this.roll = roll;
        hasCameraMoved = true;
    }

    public void setSecondaryPitch(float secondaryPitch) {
        this.secondaryPitch = secondaryPitch;
        hasCameraMoved = true;
    }

    public Matrix4f getViewMatrix() {
        // if the camera moved, recalculate the view matrix
        if (hasCameraMoved) {
            viewMatrix = RenderingMath.createViewMatrix(position, pitch + secondaryPitch, yaw, roll);
        }
        // ensures view matrix will not be recalculated next frame if the camera didn't move
        hasCameraMoved = false;

        return viewMatrix;
    }

    public static final Camera defaultCamera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);

}
