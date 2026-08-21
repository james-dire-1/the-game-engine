package templates.gameplay;

import com.james.common.simulation.collisionEngine.hitboxes.Ray;
import com.james.common.simulation.collisionEngine.math.RSTCommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.math.containers.RaySphereInfo;
import com.james.common.simulation.collisionEngine.math.containers.RayTriangleInfo;
import com.james.common.tools.Mth;
import com.james.gameplay.CameraController;
import com.james.gameplay.GameLoader;
import com.james.input.ClickInput;
import com.james.input.KeyInput;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.visuals.FogSettings;
import com.james.renderEngine.visuals.Skybox;
import com.james.simulation.ClientLevel;
import com.james.simulation.collisionEngine.ClientCollisionHandler;
import com.james.simulation.collisionEngine.RSTClientCollisionHandler;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.Player;
import com.james.tools.MousePicker;
import com.james.wrapper.EngineUtils;
import game.ui.screens.DebugScreen;
import game.ui.screens.PauseScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import templates.rendering.ModelBank;
import templates.rendering.Skyboxes;

import static com.james.input.KeyInput.isKeyDown;
import static com.james.input.KeyInput.isKeyPressed;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

public class ClientSideUpdaters {

//    private final float referenceTime;

    public static void gameLoaderInit() {
        Skybox.currentSkybox = Skyboxes.getByName(Skyboxes.DEFAULT_SKYBOX_NAME);

        FogSettings.setUseSphericalFog(FogSettings.DEFAULT_USE_SPHERICAL_FOG);
        FogSettings.setFogDensity(FogSettings.DEFAULT_DENSITY);
        FogSettings.setFogGradient(FogSettings.DEFAULT_GRADIENT);

//        this.referenceTime = Time.getCurrentTime();
    }

    private static boolean prevIsOpen;

    public static void gameLoaderUpdate(GameLoader gameLoader) {
        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_R)) {
            GLFWUtilities.lockCursor(!GLFWUtilities.isCursorLocked());
        }

        if (KeyInput.isKeyDown(GLFW_KEY_TAB)) {
            PauseScreen.toggle();
        }

        if (KeyInput.isKeyDown(GLFW_KEY_F3)) {
            DebugScreen.toggle();
        }

        if (KeyInput.isKeyDown(GLFW_KEY_B)) {
            Vector3f offset = Mth.pitchAndYawToGLCartesianCoordinates(20, Camera.defaultCamera.getPitch(), Camera.defaultCamera.getYaw(), null);
            Vector3f finalPosition = Vector3f.add(offset, Camera.defaultCamera.getPosition(), null);
            GameObject gameObject = new GameObject(ModelBank.getAbstractArt(), finalPosition);
            gameLoader.batchedGameObjectsList.addGameObject(gameObject);
        }

        if (PauseScreen.isOpen != prevIsOpen) {
            prevIsOpen = PauseScreen.isOpen;
            ClientLevel.get().events.sendChangePauseState(PauseScreen.isOpen);
            EngineUtils.gameLoader.isPaused = PauseScreen.isOpen;
        }

//        float normalizedTimeOfDay = ((Time.getCurrentTime() - referenceTime) % 1200) / 1200;
//        float xDirection = (float) Math.cos(normalizedTimeOfDay * 2 * Math.PI + Math.toRadians(80));
//        float yDirection = (float) Math.sin(normalizedTimeOfDay * 2 * Math.PI + Math.toRadians(80));
//        lightHandler.getDirectionalLight(1).setToLightDirection(xDirection, yDirection, 0);
    }

    public static void playerMoveFrame(Player player, GameObject playerGameObject, CameraController camController) {
        player.getRotation().y = -camController.getCamera().getYaw();

        if (isKeyDown(GLFW_KEY_G)) {
            player.isAffectedByGravity = !player.isAffectedByGravity;
        }

        if (KeyInput.isKeyPressed(GLFW_KEY_F)) {
            camController.firstPerson = false;
            playerGameObject.isVisible = true;
        } else {
            camController.firstPerson = true;
            playerGameObject.isVisible = false;
        }
    }

    private static final float SPEED = 15;
    private static final Vector2f reusableVelocity = new Vector2f();

    public static void playerMoveTick(Player player, Vector2f forwardDirectionVector, Vector2f rightDirectionVector) {
        float forwardSpeed = 0;
        if (isKeyPressed(GLFW_KEY_W)) {
            forwardSpeed += SPEED;
        }
        if (isKeyPressed(GLFW_KEY_S)) {
            forwardSpeed -= SPEED;
        }
        float rightSpeed = 0;
        if (isKeyPressed(GLFW_KEY_D)) {
            rightSpeed += SPEED;
        }
        if (isKeyPressed(GLFW_KEY_A)) {
            rightSpeed -= SPEED;
        }

        Vector2f.add(Mth.multiply(forwardDirectionVector, forwardSpeed), Mth.multiply(rightDirectionVector, rightSpeed), reusableVelocity);
        player.setVelocity(reusableVelocity.x, 0, -reusableVelocity.y);

        if (isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            Vector3f playerPosition = player.getPosition();
            player.setPosition(playerPosition.x, playerPosition.y + 5, playerPosition.z);
        }

        if (isKeyPressed(GLFW_KEY_SPACE)) {
            player.getFallingAndGravityState().attemptToJump();
        }
    }

    private static final MousePicker mousePicker = new MousePicker(Camera.defaultCamera);
    private static GameObject lastGameObject;
//    private final List<GameObject> lastGameObjects = new ArrayList<>();

    public static void clientLevelUpdate(ClientCollisionHandler clientCollisionHandler, RSTClientCollisionHandler rstClientCollisionHandler) {
        mousePicker.update();
        Ray ray = new Ray(Camera.defaultCamera.getPosition(), mousePicker.getCurrentRay());

        if (ClickInput.isLeftClickPressed()) {
            RayTriangleInfo rayTriangleInfo = new RayTriangleInfo();
            RSTCommonCollisionProcedure.findClosestRayIntersectionWithTriangle(ray, -1, 1000, clientCollisionHandler.cachedAABBHitboxes.values(), rayTriangleInfo);

            if (rayTriangleInfo.closestIntersectionPoint != null) {
                EngineUtils.gameLoader.batchedGameObjectsList.addGameObject(new GameObject(ModelBank.getColorAbstractArt(), rayTriangleInfo.closestIntersectionPoint, new Vector3f(), 0.5f));
//                player.setPosition(rayTriangleInfo.closestIntersectionPoint.x, rayTriangleInfo.closestIntersectionPoint.y, rayTriangleInfo.closestIntersectionPoint.z);
            }
        }

        RaySphereInfo raySphereInfo = new RaySphereInfo();
        RSTCommonCollisionProcedure.findClosestRayIntersectionWithSphere(ray, -1, rstClientCollisionHandler.cachedSphereHitboxes.values(), raySphereInfo);
//        List<AbstractSphereHitbox> sphereHitboxes = new ArrayList<>();
//        RSTCommonCollisionProcedure.findAllRayIntersectionsWithSpheres(ray, -1, rstClientCollisionHandler.cachedSphereHitboxes.values(), null, sphereHitboxes);

        if (lastGameObject != null) {
            lastGameObject.highlightFactor = 0.0f;
        }
//        for (GameObject lastGameObject : lastGameObjects) {
//            lastGameObject.highlightFactor = 0.0f;
//        }
//        lastGameObjects.clear();

        if (raySphereInfo.closestCollidedHitbox != null) {
            lastGameObject = ((CachedPhysicalObject) raySphereInfo.closestCollidedHitbox.object).getGameObject();
            lastGameObject.highlightFactor = 0.2f;
        }
//        for (AbstractSphereHitbox sphereHitbox : sphereHitboxes) {
//            GameObject gameObject = ((CachedPhysicalObject) sphereHitbox.object).getGameObject();
//            gameObject.highlightFactor = 0.3f;
//            lastGameObjects.add(gameObject);
//        }
    }

}
