package templates.gameplay;

import com.james.common.simulation.collisionEngine.hitboxes.Ray;
import com.james.common.simulation.collisionEngine.math.RSTCommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.math.containers.RaySphereInfo;
import com.james.common.simulation.collisionEngine.math.containers.RayTriangleInfo;
import com.james.common.tools.Mth;
import com.james.gameplay.CameraController;
import com.james.gameplay.GameLoader;
import com.james.gameplay.PlayerHandler;
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
import templates.common.GlobalConstants;
import templates.rendering.ModelBank;
import templates.rendering.Skyboxes;

import static com.james.input.KeyInput.isKeyDown;
import static com.james.input.KeyInput.isKeyPressed;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

public class ClientSideUpdaters {

    public static class GameLoaderUpdater implements GameLoader.Updater {

        private boolean prevIsOpen;

        @Override
        public void init() {
            Skybox.currentSkybox = Skyboxes.getByName(Skyboxes.DEFAULT_SKYBOX_NAME);

            FogSettings.setUseSphericalFog(FogSettings.DEFAULT_USE_SPHERICAL_FOG);
            FogSettings.setFogDensity(FogSettings.DEFAULT_DENSITY);
            FogSettings.setFogGradient(FogSettings.DEFAULT_GRADIENT);
        }

        @Override
        public void update(GameLoader gameLoader) {
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

    }

    public static class PlayerHandlerUpdater implements PlayerHandler.Updater {

        private static final float MAX_REGULAR_SPEED = 15.0f;
        private static final float MAX_SPRINT_SPEED = 22.0f;
        private static final float MAX_CROUCH_SPEED = 3.0f;
        private static final float STANDARD_SPEED_INCREMENT = 5.0f;
        private static final float IN_AIR_MULTIPLIER = 0.2f;

        private float forwardSpeed;
        private float rightSpeed;

        private final Vector2f reusableDeltaLocalDirection = new Vector2f();
        private final Vector2f reusableVelocity = new Vector2f();

        @Override
        public void init() {
            forwardSpeed = 0.0f;
            rightSpeed = 0.0f;
        }

        @Override
        public void moveTick(Player player, Vector2f forwardDirectionVector, Vector2f rightDirectionVector) {
            boolean wantsToMoveStraight = isKeyPressed(GLFW_KEY_W) ^ isKeyPressed(GLFW_KEY_S);
            boolean wantsToMoveSideways = isKeyPressed(GLFW_KEY_D) ^ isKeyPressed(GLFW_KEY_A);
            boolean onGround = player.getCollisionDetails().onGround;
            boolean crouching = isKeyPressed(GLFW_KEY_C) && onGround;
            boolean sprinting = isKeyPressed(GLFW_KEY_LEFT_SHIFT) && !crouching;

            if (onGround) {
                if (!wantsToMoveStraight) {
                    if (forwardSpeed > 0.0f) forwardSpeed -= STANDARD_SPEED_INCREMENT;
                    else if (forwardSpeed < 0.0f) forwardSpeed += STANDARD_SPEED_INCREMENT;

                    if (Math.abs(forwardSpeed) < STANDARD_SPEED_INCREMENT) forwardSpeed = 0.0f;
                }
                if (!wantsToMoveSideways) {
                    if (rightSpeed > 0.0f) rightSpeed -= STANDARD_SPEED_INCREMENT;
                    else if (rightSpeed < 0.0f) rightSpeed += STANDARD_SPEED_INCREMENT;

                    if (Math.abs(rightSpeed) < STANDARD_SPEED_INCREMENT) rightSpeed = 0.0f;
                }

                if (isKeyPressed(GLFW_KEY_W) && forwardSpeed < 0.0f) forwardSpeed = 0.0f;
                if (isKeyPressed(GLFW_KEY_S) && forwardSpeed > 0.0f) forwardSpeed = 0.0f;
                if (isKeyPressed(GLFW_KEY_D) && rightSpeed < 0.0f) rightSpeed = 0.0f;
                if (isKeyPressed(GLFW_KEY_A) && rightSpeed > 0.0f) rightSpeed = 0.0f;
            }

            float deltaForwardInput = 0.0f;
            float deltaRightInput = 0.0f;

            if (isKeyPressed(GLFW_KEY_W)) deltaForwardInput += 1.0f;
            if (isKeyPressed(GLFW_KEY_S)) deltaForwardInput -= 1.0f;
            if (isKeyPressed(GLFW_KEY_D)) deltaRightInput += 1.0f;
            if (isKeyPressed(GLFW_KEY_A)) deltaRightInput -= 1.0f;

            float speedIncrementThisTick = STANDARD_SPEED_INCREMENT;
            if (!onGround) speedIncrementThisTick *= IN_AIR_MULTIPLIER;

            reusableDeltaLocalDirection.set(deltaRightInput, deltaForwardInput);
            if (reusableDeltaLocalDirection.length() != 0.0f) reusableDeltaLocalDirection.normalise();
            reusableDeltaLocalDirection.scale(speedIncrementThisTick);

            forwardSpeed += reusableDeltaLocalDirection.y;
            rightSpeed += reusableDeltaLocalDirection.x;

            float speedClamp;
            if (sprinting) speedClamp = MAX_SPRINT_SPEED;
            else if (crouching) speedClamp = MAX_CROUCH_SPEED;
            else speedClamp = MAX_REGULAR_SPEED;

            if (forwardSpeed > speedClamp) forwardSpeed = speedClamp;
            else if (forwardSpeed < -speedClamp) forwardSpeed = -speedClamp;
            if (rightSpeed > speedClamp) rightSpeed = speedClamp;
            else if (rightSpeed < -speedClamp) rightSpeed = -speedClamp;

            Vector2f.add(Mth.multiply(forwardDirectionVector, forwardSpeed), Mth.multiply(rightDirectionVector, rightSpeed), reusableVelocity);
            player.setVelocity(reusableVelocity.x, 0, -reusableVelocity.y);

            if (isKeyPressed(GLFW_KEY_SPACE)) {
                player.getFallingAndGravityState().attemptToJump();
            }

            if (GlobalConstants.IS_PLAYER_DEBUG && isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
                Vector3f playerPosition = player.getPosition();
                player.setPosition(playerPosition.x, playerPosition.y + 5, playerPosition.z);
            }
        }

        @Override
        public void moveFrame(Player player, GameObject playerGameObject, CameraController camController) {
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

    }

    public static class ClientLevelUpdater implements ClientLevel.Updater {

        private final MousePicker mousePicker = new MousePicker(Camera.defaultCamera);
        private GameObject lastGameObject;

        @Override
        public void update(ClientCollisionHandler clientCollisionHandler, RSTClientCollisionHandler rstClientCollisionHandler) {
            mousePicker.update();
            Ray ray = new Ray(Camera.defaultCamera.getPosition(), mousePicker.getCurrentRay());

            if (ClickInput.isLeftClickPressed()) {
                RayTriangleInfo rayTriangleInfo = new RayTriangleInfo();
                RSTCommonCollisionProcedure.findClosestRayIntersectionWithTriangle(ray, -1, 1000, clientCollisionHandler.cachedAABBHitboxes.values(), rayTriangleInfo);

                if (rayTriangleInfo.closestIntersectionPoint != null) {
                    EngineUtils.gameLoader.batchedGameObjectsList.addGameObject(new GameObject(ModelBank.getColorAbstractArt(), rayTriangleInfo.closestIntersectionPoint, new Vector3f(), 0.5f));
                }
            }

            RaySphereInfo raySphereInfo = new RaySphereInfo();
            RSTCommonCollisionProcedure.findClosestRayIntersectionWithSphere(ray, -1, rstClientCollisionHandler.cachedSphereHitboxes.values(), raySphereInfo);

            if (lastGameObject != null) {
                lastGameObject.highlightFactor = 0.0f;
            }

            if (raySphereInfo.closestCollidedHitbox != null) {
                lastGameObject = ((CachedPhysicalObject) raySphereInfo.closestCollidedHitbox.object).getGameObject();
                lastGameObject.highlightFactor = 0.2f;
            }
        }

    }

}
