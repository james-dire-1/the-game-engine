package templates.gameplay;

import com.james.renderEngine.gameObjects.Camera;
import com.james.input.MouseMoveInput;
import com.james.renderEngine.utilities.GLFWUtilities;
import templates.settings.UserSettings;
import org.lwjgl.util.vector.Vector3f;

public class CameraController {

    public boolean firstPerson = true;

    private final Camera camera;
    private final Vector3f target;
    private final float distanceFromTarget;
    private final Vector3f firstPersonOffset = new Vector3f(0, 0, 0);

    public CameraController(Camera camera, Vector3f target, float distanceFromTarget) {
        this.camera = camera;
        this.target = target;
        this.distanceFromTarget = distanceFromTarget;
    }

    public void setFirstPersonOffset(float offsetX, float offsetY, float offsetZ) {
        this.firstPersonOffset.x = offsetX;
        this.firstPersonOffset.y = offsetY;
        this.firstPersonOffset.z = offsetZ;
    }

    public void update() {
        if (GLFWUtilities.isCursorLocked()) {
            camera.increasePitch((float) MouseMoveInput.getDeltaY() * UserSettings.MOUSE_SENSITIVITY);
            camera.increaseYaw((float) MouseMoveInput.getDeltaX() * UserSettings.MOUSE_SENSITIVITY);
        }

        Vector3f cameraPosition;
        if (!firstPerson) {
            float xAndZDistance = (float) Math.cos(Math.toRadians(camera.getPitch())) * distanceFromTarget;
            float cameraY = (float) Math.sin(Math.toRadians(camera.getPitch())) * distanceFromTarget;

            float cameraX = (float) Math.cos(Math.toRadians(90 + camera.getYaw())) * xAndZDistance;
            float cameraZ = (float) Math.sin(Math.toRadians(90 + camera.getYaw())) * xAndZDistance;

            cameraPosition = new Vector3f(cameraX, cameraY, cameraZ);
        } else {
            cameraPosition = firstPersonOffset;
        }

        cameraPosition = Vector3f.add(target, cameraPosition, null);
        camera.setPosition(cameraPosition);
    }

    public Camera getCamera() { return camera; }

}
