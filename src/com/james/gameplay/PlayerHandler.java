package com.james.gameplay;

import com.james.simulation.objects.Player;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.tools.Time;
import com.james.tools.Vector3fInterpolator;
import org.lwjgl.util.vector.Vector3f;
import templates.gameplay.ClientSideUpdaters;
import templates.rendering.ModelBank;
import com.james.simulation.ClientLevel;
import org.lwjgl.util.vector.Vector2f;

// TODO: 2026-08-17 outdated documentation?
/**
 * Client-side class which handles much of the player logic, including aspects regarding simulation,
 * user input, camera movement, and rendering (i.e. it creates the Player's GameObject).
 */
public class PlayerHandler {

    public static Model prop_playerModel = ModelBank.getAbstractArt();
    public static float prop_cameraDistanceFromPlayer = 15;
    public static final Vector3f prop_camFirstPersonOffset = new Vector3f(0, 1.75f, 0);

    public static String localUsername;
    public static float[] localColor;

    private final GameObject gameObject;
    private final CameraController camController;

    public GameObject getGameObject() { return gameObject; }
    public CameraController getCamController() { return camController; }

    /**
     * Creates the Player's GameObject and its CameraController.
     */
    public PlayerHandler(GameLoader gameLoader) {
        ClientLevel clientLevel = ClientLevel.get();
        Player player = clientLevel.getPlayer();

        this.gameObject = new GameObject(prop_playerModel, new Vector3f(player.getPosition()), player.getRotation(), 1);
        gameObject.isVisible = false;
        gameLoader.batchedGameObjectsList.addGameObject(gameObject);

        this.camController = new CameraController(gameLoader.prop_camera, gameObject.getPosition(), prop_cameraDistanceFromPlayer);
        camController.setFirstPersonOffset(prop_camFirstPersonOffset.x, prop_camFirstPersonOffset.y, prop_camFirstPersonOffset.z);

        Vector3fInterpolator.secondsPerGameTick = clientLevel.secondsPerGameTick;
    }

    /**
     * Updates player rotation, which happens every frame, simulation logic, which happens every
     * game tick, and the camera controller, which happens every frame. During the simulation update,
     * sets the velocity of the player based on the direction vectors and user input, and updates client-side
     * collisions.
     */
    public void update() {
        ClientLevel clientLevel = ClientLevel.get();
        Player player = clientLevel.getPlayer();

        ClientSideUpdaters.playerMoveFrame(player, gameObject, camController);

        if (Time.getCurrentTime() - lastTime >= clientLevel.secondsPerGameTick) {
            lastTime = Time.getCurrentTime();

            player.updatePrevPosition();
            calculateDirectionVectors();

            ClientSideUpdaters.playerMoveTick(player, forwardDirectionVector, rightDirectionVector);

            clientLevel.update();
        }

        Vector3fInterpolator.interpolate(player.getPrevPosition(), player.getPosition(), gameObject.getPosition(), lastTime, Time.getCurrentTime());
        camController.update();
    }

    /**
     * Calculates the two direction vectors that are important for player movement: forward and right.
     */
    private void calculateDirectionVectors() {
        Camera camera = camController.getCamera();

        float forwardAngleInUnitCircle = 90 - camera.getYaw();
        forwardDirectionVector.x = (float) Math.cos(Math.toRadians(forwardAngleInUnitCircle));
        forwardDirectionVector.y = (float) Math.sin(Math.toRadians(forwardAngleInUnitCircle));

        float rightAngleInUnitCircle = -camera.getYaw();
        rightDirectionVector.x = (float) Math.cos(Math.toRadians(rightAngleInUnitCircle));
        rightDirectionVector.y = (float) Math.sin(Math.toRadians(rightAngleInUnitCircle));
    }

    private float lastTime = Time.getCurrentTime();
    private final Vector2f forwardDirectionVector = new Vector2f();
    private final Vector2f rightDirectionVector = new Vector2f();

}
