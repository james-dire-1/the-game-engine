package templates.gameplay;

import com.james.common.simulation.collisionEngine.hitboxes.Ray;
import com.james.common.simulation.collisionEngine.math.RSTCommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.math.containers.RaySphereInfo;
import com.james.common.simulation.collisionEngine.math.containers.RayTriangleInfo;
import com.james.common.simulation.details.FallingAndGravityState;
import com.james.common.tools.Mth;
import com.james.gameplay.CameraController;
import com.james.gameplay.GameLoader;
import com.james.gameplay.PlayerHandler;
import com.james.input.ClickInput;
import com.james.input.KeyInput;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.visuals.FogSettings;
import com.james.renderEngine.visuals.Skybox;
import com.james.simulation.ClientLevel;
import com.james.simulation.collisionEngine.ClientCollisionHandler;
import com.james.simulation.collisionEngine.RSTClientCollisionHandler;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.Player;
import com.james.tools.MousePicker;
import com.james.tools.Time;
import com.james.wrapper.EngineUtils;
import game.ui.screens.DebugScreen;
import game.ui.screens.PauseScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import templates.common.GlobalConstants;
import templates.rendering.ModelBank;
import templates.rendering.Skyboxes;
import templates.settings.UserSettings;

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

            if (GlobalConstants.IS_PLAYER_DEBUG && KeyInput.isKeyDown(GLFW_KEY_B)) {
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

        @Override
        public void init() {
            sprinting = false;
            crouching = false;

            forwardSpeed = 0.0f;
            rightSpeed = 0.0f;
        }

        private static final float MAX_REGULAR_SPEED = 15.0f;
        private static final float MAX_SPRINT_SPEED = 22.0f;
        private static final float MAX_CROUCH_SPEED = 3.0f;
        private static final float MAX_FALL_SPEED = 30.0f;
        private static final float STANDARD_SPEED_INCREMENT = 5.0f;
        private static final float IN_AIR_MULTIPLIER = 0.2f;

        private boolean wantsToMoveStraight;
        private boolean wantsToMoveSideways;
        private boolean onGround;
        private boolean sprinting;
        private boolean crouching;

        private float forwardSpeed;
        private float rightSpeed;

        private final Vector2f reusableDeltaLocalVelocity = new Vector2f();
        private final Vector2f reusableVelocity = new Vector2f();
        private final Vector2f reusableDeltaVelocity = new Vector2f();

        @Override
        public void updateTick(Player player, Vector2f forwardDirectionVector, Vector2f rightDirectionVector) {
            wantsToMoveStraight = isKeyPressed(GLFW_KEY_W) ^ isKeyPressed(GLFW_KEY_S);
            wantsToMoveSideways = isKeyPressed(GLFW_KEY_D) ^ isKeyPressed(GLFW_KEY_A);
            onGround = player.getCollisionDetails().onGround;
            crouching = isKeyPressed(GLFW_KEY_C) && onGround;
            sprinting = isKeyPressed(GLFW_KEY_LEFT_SHIFT) && !crouching;

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

            reusableDeltaLocalVelocity.set(deltaRightInput, deltaForwardInput);
            if (reusableDeltaLocalVelocity.length() != 0.0f) reusableDeltaLocalVelocity.normalise();
            reusableDeltaLocalVelocity.scale(speedIncrementThisTick);

            float speedClamp;

            if (onGround) {
                forwardSpeed += reusableDeltaLocalVelocity.y;
                rightSpeed += reusableDeltaLocalVelocity.x;

                if (sprinting) speedClamp = MAX_SPRINT_SPEED;
                else if (crouching) speedClamp = MAX_CROUCH_SPEED;
                else speedClamp = MAX_REGULAR_SPEED;

                if (forwardSpeed > speedClamp) forwardSpeed = speedClamp;
                else if (forwardSpeed < -speedClamp) forwardSpeed = -speedClamp;
                if (rightSpeed > speedClamp) rightSpeed = speedClamp;
                else if (rightSpeed < -speedClamp) rightSpeed = -speedClamp;

                Vector2f.add(Mth.multiply(forwardDirectionVector, forwardSpeed), Mth.multiply(rightDirectionVector, rightSpeed), reusableVelocity);
            } else {
                reusableDeltaVelocity.set(Vector2f.add(Mth.multiply(forwardDirectionVector, reusableDeltaLocalVelocity.y), Mth.multiply(rightDirectionVector, reusableDeltaLocalVelocity.x), null));
                reusableVelocity.translate(reusableDeltaVelocity.x, reusableDeltaVelocity.y);

                speedClamp = MAX_FALL_SPEED;
            }

            if (reusableVelocity.length() > speedClamp) {
                reusableVelocity.normalise();
                reusableVelocity.scale(speedClamp);
            }

            player.setVelocity(reusableVelocity.x, 0, -reusableVelocity.y);

            if (isKeyPressed(GLFW_KEY_SPACE)) {
                player.getFallingAndGravityState().attemptToJump();
            }

            if (GlobalConstants.IS_PLAYER_DEBUG && isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
                Vector3f playerPosition = player.getPosition();
                player.setPosition(playerPosition.x, playerPosition.y + 5.0f, playerPosition.z);
            }
        }

        @Override
        public void updateFrame(Player player, GameObject playerGameObject, CameraController camController) {
            player.getRotation().y = -camController.getCamera().getYaw();

            if (KeyInput.isKeyPressed(GLFW_KEY_F)) {
                camController.firstPerson = false;
                playerGameObject.isVisible = true;
            } else {
                camController.firstPerson = true;
                playerGameObject.isVisible = false;
            }

            if (GlobalConstants.IS_PLAYER_DEBUG && isKeyDown(GLFW_KEY_G)) {
                player.isAffectedByGravity = !player.isAffectedByGravity;
            }
        }

        private static final float MAX_BOBBING_AMPLITUDE = 30.0f;
        private static final float SECS_TO_CHANGE_BOBBING_AMPLITUDE = 0.5f;
        private BobbingAmplitudeType lastBobbingAmplitudeType = BobbingAmplitudeType.NONE;
        private enum BobbingAmplitudeType { NONE, MOVING }
        private float startBobbingAmplitude;
        private float targetBobbingAmplitude;
        private float currentBobbingAmplitude;
        private float startTimeBobbingAmplitudeChange = -1.0f;

        private static final float MAX_CAMERA_JOLT_START_JUMP = 1.75f;
        private static final float MAX_CAMERA_JOLT_END_JUMP = 0.5f;
        private static final float SECS_TO_DO_CAMERA_JOLT = 0.25f;
        private static final float MIN_TIME_BETWEEN_END_JUMP_JOLTS = 0.5f;
        private JumpJoltType lastJumpJoltType = JumpJoltType.NONE;
        private enum JumpJoltType { NONE, START_JUMP, END_JUMP }
        private float startTimeCameraJolt = -1.0f;
        private FallingAndGravityState.State prevState;
        private float lastOnGroundFirmlyTime;

        private static final float SECS_TO_SWITCH_FOV = 0.25f;
        private FovType lastFovType = FovType.REGULAR;
        private enum FovType { REGULAR, SPRINT, CROUCH }
        private float startFov;
        private float targetFov;
        private float startTimeFovSwitch = -1.0f;

        private static final float MAX_PITCH_CHANGE_DUE_TO_FALL = 15.0f;
        private static final float SECS_TO_PITCH_CHANGE_START_DUE_TO_FALL = 0.5f;
        private static final float SECS_TO_MAX_PITCH_CHANGE_DUE_TO_FALL = 3.0f;
        private static final float SECS_TO_SWITCH_PITCH_AFTER_FALL = 0.5f;
        private PitchType lastPitchType = PitchType.ON_GROUND;
        private enum PitchType { ON_GROUND, FALLING, RECOVERING }
        private float startSecondaryPitch;
        private float startTimePitchChange = -1.0f;

        @Override
        public void updateCameraFrame(Camera camera, Vector2f forwardDirectionVector, Vector2f rightDirectionVector, Player player) {
            if (onGround && (wantsToMoveStraight || wantsToMoveSideways)) {
                if (lastBobbingAmplitudeType != BobbingAmplitudeType.MOVING) {
                    lastBobbingAmplitudeType = BobbingAmplitudeType.MOVING;
                    startBobbingAmplitude = currentBobbingAmplitude;
                    targetBobbingAmplitude = MAX_BOBBING_AMPLITUDE;
                    startTimeBobbingAmplitudeChange = Time.getCurrentTime();
                }
            } else {
                if (lastBobbingAmplitudeType != BobbingAmplitudeType.NONE) {
                    lastBobbingAmplitudeType = BobbingAmplitudeType.NONE;
                    startBobbingAmplitude = currentBobbingAmplitude;
                    targetBobbingAmplitude = 0.0f;
                    startTimeBobbingAmplitudeChange = Time.getCurrentTime();
                }
            }

            if (startTimeBobbingAmplitudeChange > 0.0f) {
                float timeSinceBobbingAmplitudeChangeStart = Time.getCurrentTime() - startTimeBobbingAmplitudeChange;

                if (timeSinceBobbingAmplitudeChangeStart < SECS_TO_CHANGE_BOBBING_AMPLITUDE) {
                    float normalizedTimeProgress = timeSinceBobbingAmplitudeChangeStart / SECS_TO_CHANGE_BOBBING_AMPLITUDE;
                    float bobbingAmplitudeDifference = targetBobbingAmplitude - startBobbingAmplitude;
                    currentBobbingAmplitude = startBobbingAmplitude + normalizedTimeProgress * bobbingAmplitudeDifference;
                } else {
                    startTimeBobbingAmplitudeChange = -1.0f;
                    currentBobbingAmplitude = targetBobbingAmplitude;
                }
            }

            if (currentBobbingAmplitude > 0.0f) {
                float currentAngle = currentBobbingAmplitude * (float) Math.sin(2.0f * Math.PI * Time.getCurrentTime()) - 90.0f;
                float cameraOffsetY = (float) Math.sin(Math.toRadians(currentAngle)) + 1.0f;

                Vector3f rightDirectionVectorR3 = new Vector3f(rightDirectionVector.x, 0, -rightDirectionVector.y);
                Vector3f forwardDirectionVectorR3 = new Vector3f(forwardDirectionVector.x, 0, -forwardDirectionVector.y);
                Vector3f upDirectionVector = Vector3f.cross(rightDirectionVectorR3, forwardDirectionVectorR3, null);
                Vector3f cameraOffset = Mth.multiply(upDirectionVector, cameraOffsetY);

                camera.translate(cameraOffset);
            }

            FallingAndGravityState.State state = player.getFallingAndGravityState().getState();

            if (prevState != state && !(prevState == FallingAndGravityState.State.ON_GROUND_RECOVERING && state == FallingAndGravityState.State.ON_GROUND_FIRMLY)) {
//            if (prevState != state) {
                prevState = state;

                if (state == FallingAndGravityState.State.BEGAN_JUMP) {
                    if (lastJumpJoltType != JumpJoltType.START_JUMP) {
                        lastJumpJoltType = JumpJoltType.START_JUMP;
                        startTimeCameraJolt = Time.getCurrentTime();
                    }
                } else if (state == FallingAndGravityState.State.ON_GROUND_RECOVERING || state == FallingAndGravityState.State.ON_GROUND_FIRMLY) {
//                } else if (state == FallingAndGravityState.State.ON_GROUND_FIRMLY) {
                    if (lastJumpJoltType != JumpJoltType.END_JUMP && Time.getCurrentTime() - lastOnGroundFirmlyTime > MIN_TIME_BETWEEN_END_JUMP_JOLTS) {
                        lastJumpJoltType = JumpJoltType.END_JUMP;
                        startTimeCameraJolt = Time.getCurrentTime();
                    }
                }
            }

            if (state == FallingAndGravityState.State.ON_GROUND_FIRMLY) {
                lastOnGroundFirmlyTime = Time.getCurrentTime();
            }

            if (startTimeCameraJolt > 0.0f) {
                float timeSinceJoltStart = Time.getCurrentTime() - startTimeCameraJolt;

                if (lastJumpJoltType == JumpJoltType.START_JUMP || lastJumpJoltType == JumpJoltType.END_JUMP) {
                    if (timeSinceJoltStart < SECS_TO_DO_CAMERA_JOLT) {
                        float normalizedTimeProgress = timeSinceJoltStart / SECS_TO_DO_CAMERA_JOLT;
                        float cameraJoltThisFrame;
                        float easingProgress = cameraTranslateJoltCosFunction(Math.min(normalizedTimeProgress + 0.25f, 1.0f));
                        if (lastJumpJoltType == JumpJoltType.START_JUMP) {
                            cameraJoltThisFrame = easingProgress * MAX_CAMERA_JOLT_START_JUMP;
                        } else {
                            cameraJoltThisFrame = easingProgress * MAX_CAMERA_JOLT_END_JUMP;
                        }
                        camera.getPosition().y += cameraJoltThisFrame;
                    } else {
                        startTimeCameraJolt = -1.0f;
                        lastJumpJoltType = JumpJoltType.NONE;
                    }
                }
            }

            if (sprinting) {
                if (lastFovType != FovType.SPRINT) {
                    lastFovType = FovType.SPRINT;
                    startFov = UserSettings.fov;
                    targetFov = UserSettings.DEFAULT_SPRINT_FOV;
                    startTimeFovSwitch = Time.getCurrentTime();
                }
            } else if (crouching) {
                if (lastFovType != FovType.CROUCH) {
                    lastFovType = FovType.CROUCH;
                    startFov = UserSettings.fov;
                    targetFov = UserSettings.DEFAULT_CROUCH_FOV;
                    startTimeFovSwitch = Time.getCurrentTime();
                }
            } else {
                if (lastFovType != FovType.REGULAR) {
                    lastFovType = FovType.REGULAR;
                    startFov = UserSettings.fov;
                    targetFov = UserSettings.DEFAULT_REGULAR_FOV;
                    startTimeFovSwitch = Time.getCurrentTime();
                }
            }

            if (startTimeFovSwitch > 0.0f) {
                float timeSinceFovSwitchStart = Time.getCurrentTime() - startTimeFovSwitch;

                if (timeSinceFovSwitchStart < SECS_TO_SWITCH_FOV) {
                    float normalizedTimeProgress = timeSinceFovSwitchStart / SECS_TO_SWITCH_FOV;
                    float fovDifference = targetFov - startFov;
                    float fovThisFrame = startFov + normalizedTimeProgress * fovDifference;
                    MasterRenderer.setFov(fovThisFrame);
                } else {
                    startTimeFovSwitch = -1.0f;
                    MasterRenderer.setFov(targetFov);
                }
            }

            if (state == FallingAndGravityState.State.FALLING || state == FallingAndGravityState.State.FALLING_FROM_JUMP) {
                if (lastPitchType != PitchType.FALLING) {
                    lastPitchType = PitchType.FALLING;
                    startSecondaryPitch = camera.getSecondaryPitch();
                    startTimePitchChange = Time.getCurrentTime();
                }
            } else if (state == FallingAndGravityState.State.ON_GROUND_RECOVERING) {
                if (lastPitchType != PitchType.RECOVERING) {
                    lastPitchType = PitchType.RECOVERING;
                    startSecondaryPitch = camera.getSecondaryPitch();
                    startTimePitchChange = Time.getCurrentTime();
                }
            } else {
                if (lastPitchType != PitchType.ON_GROUND) {
                    lastPitchType = PitchType.ON_GROUND;
                    camera.setSecondaryPitch(0.0f);
                    startTimePitchChange = -1.0f;
                }
            }

            if (startTimePitchChange > 0.0f) {
                float timeSincePitchChangeStart = Time.getCurrentTime() - startTimePitchChange;

                if (lastPitchType == PitchType.FALLING) {
                    if (timeSincePitchChangeStart > SECS_TO_PITCH_CHANGE_START_DUE_TO_FALL) {
                        if (timeSincePitchChangeStart < SECS_TO_PITCH_CHANGE_START_DUE_TO_FALL + SECS_TO_MAX_PITCH_CHANGE_DUE_TO_FALL) {
                            float normalizedTimeProgress = (timeSincePitchChangeStart - SECS_TO_PITCH_CHANGE_START_DUE_TO_FALL) / SECS_TO_MAX_PITCH_CHANGE_DUE_TO_FALL;
                            float easingProgress = pitchSmoothChangeEasingFunction(normalizedTimeProgress);
                            float secondaryPitchDifference = MAX_PITCH_CHANGE_DUE_TO_FALL - startSecondaryPitch;
                            float secondaryPitchThisFrame = startSecondaryPitch + easingProgress * secondaryPitchDifference;
                            camera.setSecondaryPitch(secondaryPitchThisFrame);
                        } else {
                            startTimePitchChange = -1.0f;
                            camera.setSecondaryPitch(MAX_PITCH_CHANGE_DUE_TO_FALL);
                        }
                    }
                } else if (lastPitchType == PitchType.RECOVERING) {
                    if (timeSincePitchChangeStart < SECS_TO_SWITCH_PITCH_AFTER_FALL) {
                        float normalizedTimeProgress = timeSincePitchChangeStart / SECS_TO_SWITCH_PITCH_AFTER_FALL;
                        float easingProgress = pitchJoltEasingFunction(normalizedTimeProgress);
                        float secondaryPitchDifference = 0.0f - startSecondaryPitch;
                        float secondaryPitchThisFrame = startSecondaryPitch + easingProgress * secondaryPitchDifference;
                        camera.setSecondaryPitch(secondaryPitchThisFrame);
                    } else {
                        startTimePitchChange = -1.0f;
                        camera.setSecondaryPitch(0.0f);
                    }
                }
            }
        }

        private static float cameraTranslateJoltCosFunction(float input) {
            return 0.5f * (float) Math.cos(2.0f * Math.PI * input) - 0.5f;
        }

        private static float pitchSmoothChangeEasingFunction(float input) {
            return 0.5f - 0.5f * (float) Math.cos(Math.PI * input);
        }

        private static float pitchJoltEasingFunction(float input) {
            float inputProcessed = 6.0f * (input - 0.3f);
            float firstTerm = 1.0f / (1.0f + (float) Math.exp(-inputProcessed));
            float secondTerm = 1.5f * (float) Math.exp(-inputProcessed * inputProcessed);

            return firstTerm + secondTerm;
        }

    }

    public static class ClientLevelUpdater implements ClientLevel.Updater {

        private final MousePicker mousePicker = new MousePicker(Camera.defaultCamera);
        private GameObject lastGameObject;

        @Override
        public void update(ClientCollisionHandler clientCollisionHandler, RSTClientCollisionHandler rstClientCollisionHandler) {
            mousePicker.update();
            Ray ray = new Ray(Camera.defaultCamera.getPosition(), mousePicker.getCurrentRay());

            if (GlobalConstants.IS_PLAYER_DEBUG && ClickInput.isLeftClickPressed()) {
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
