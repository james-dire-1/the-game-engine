package com.james.main;

import com.james.renderEngine.gameObjects.Camera;
import com.james.input.MouseMoveInput;
import com.james.renderEngine.utilities.GLFWUtilities;
import org.lwjgl.util.vector.Vector3f;

public class CameraController {

    private final Camera camera;
    private final Vector3f target;
    private final float distanceFromTarget;
    private Vector3f globalOffset;

    public CameraController(Camera camera, Vector3f target, float distanceFromTarget) {
        this.camera = camera;
        this.target = target;
        this.distanceFromTarget = distanceFromTarget;
    }

    public void setGlobalOffset(Vector3f globalOffset) {
        this.globalOffset = globalOffset;
    }

    public void update() {
        if (GLFWUtilities.isCursorLocked()) {
            camera.increasePitch((float) MouseMoveInput.getDeltaY());
            camera.increaseYaw((float) MouseMoveInput.getDeltaX());
        }

        float xAndZDistance = (float) Math.cos(Math.toRadians(camera.getPitch())) * distanceFromTarget;
        float cameraY = (float) Math.sin(Math.toRadians(camera.getPitch())) * distanceFromTarget;

        float cameraX = (float) Math.cos(Math.toRadians(90 + camera.getYaw())) * xAndZDistance;
        float cameraZ = (float) Math.sin(Math.toRadians(90 + camera.getYaw())) * xAndZDistance;

        Vector3f cameraOffset = new Vector3f(cameraX, cameraY, cameraZ);
        Vector3f cameraPosition;
        if (globalOffset == null) {
            cameraPosition = Vector3f.add(target, cameraOffset, null);
        } else {
            cameraPosition = Vector3f.add(Vector3f.add(target, cameraOffset, null), globalOffset, null);
        }

        camera.setPosition(cameraPosition);
    }

    public Camera getCamera() { return camera; }

}
