package com.james.tools;

import com.james.renderEngine.gameObjects.Camera;
import com.james.input.MouseMoveInput;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.rendering.MasterRenderer;
import com.james.renderEngine.utilities.GLFWUtilities;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

// Used ThinMatrix video
public class MousePicker {

    private static Vector3f currentRay;
    public static Vector3f getCurrentRay() { return currentRay; }

    private static Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);

    public static void update() {
        if (MasterRenderer.isNewProjectionMatrix()) {
            projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        }
        currentRay = calculateMouseRay();
    }

    private static Vector3f calculateMouseRay() {
        float mouseX = (float) (GLFWUtilities.isCursorLocked() ? WindowResizeInput.width/2 : MouseMoveInput.getXPos());
        float mouseY = (float) (GLFWUtilities.isCursorLocked() ? WindowResizeInput.height/2 : MouseMoveInput.getYPos());
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1, 1);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        return toWorldCoords(eyeCoords);
    }

    private static Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
        float normalizedX = mouseX / WindowResizeInput.width * 2 - 1;
        float normalizedY = -(mouseY / WindowResizeInput.height * 2 - 1);
        return new Vector2f(normalizedX, normalizedY);
    }

    private static Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjectionMatrix = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjectionMatrix, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1, 0);
    }

    private static Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = Matrix4f.invert(Camera.defaultCamera.getViewMatrix(), null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

}
