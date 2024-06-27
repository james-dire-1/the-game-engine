package game.player;

import com.james.simulation.collisionEngine.ClientCollisionHandler;
import com.james.simulation.collisionEngine.hitboxes.PlayerHitbox;
import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.input.KeyInput;
import com.james.tools.BatchedGameObjectsList;
import com.james.tools.CameraController;
import com.james.common.tools.Mth;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.tools.Time;
import game.communication.ClientPacketReceiveActions;
import game.main.GameLoader;
import game.main.Main;
import game.rendering.ModelBank;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Client-side class which handles much of the player logic, including aspects regarding simulation,
 * user input, camera movement, and rendering (i.e. it creates the Player's GameObject).
 */
public class PlayerHandler {

    private final Player player;
    private final CameraController camController;
    private final Camera camera;

    private final Vector2f forwardDirectionVector = new Vector2f();
    private final Vector2f rightDirectionVector = new Vector2f();

    private float lastTime = Time.getCurrentTime();

    private static final float SPEED = 15;

    /**
     * Creates the Player, its GameObject, its CameraController, and sets relevant fields in
     * ClientPacketReceiveActions.
     * @see ClientPacketReceiveActions
     */
    public PlayerHandler(BatchedGameObjectsList batchedGameObjectsList, LevelProperties levelProperties, Vector3f position, Camera camera) {
        this.player = new Player(levelProperties, position);

        Model model = ModelBank.getAbstractArt();
        GameObject gameObject = new GameObject(model, player.getPosition(), player.getRotation(), 1);
        batchedGameObjectsList.addGameObject(gameObject);

        this.camController = new CameraController(camera, gameObject.getPosition(), 20);
        this.camera = this.camController.getCamera();

        ClientPacketReceiveActions.player = this.player;
        ClientPacketReceiveActions.playerHitbox = new PlayerHitbox(this.player, EllipsoidDimensions.get(1, 1, 1));
    }

    /**
     * Updates player rotation, which happens every frame, simulation logic, which happens every
     * game tick, and the camera controller, which happens every frame. During the simulation update,
     * sets the velocity of the player based on the direction vectors and user input, and updates client-side
     * collisions.
     */
    public void update() {
        player.getRotation().y = -camera.getYaw();

        if (Time.getCurrentTime() - lastTime >= ClientPacketReceiveActions.levelProperties.secondsPerGameTick) {
            lastTime = Time.getCurrentTime();
            calculateDirectionVectors();

            float forwardSpeed = 0;
            if (KeyInput.isKeyPressed(GLFW_KEY_W)) {
                forwardSpeed += SPEED;
            }
            if (KeyInput.isKeyPressed(GLFW_KEY_S)) {
                forwardSpeed -= SPEED;
            }
            float rightSpeed = 0;
            if (KeyInput.isKeyPressed(GLFW_KEY_D)) {
                rightSpeed += SPEED;
            }
            if (KeyInput.isKeyPressed(GLFW_KEY_A)) {
                rightSpeed -= SPEED;
            }

            Vector2f velocity = Vector2f.add(Mth.multiply(forwardDirectionVector, forwardSpeed), Mth.multiply(rightDirectionVector, rightSpeed), null);
            player.setVelocity(velocity.x, 0, -velocity.y);

//            player.update();
            ClientCollisionHandler.update();
        }

        camController.update();
    }

    /**
     * Calculates the two direction vectors that are important for player movement: forward and right.
     */
    private void calculateDirectionVectors() {
        float forwardAngleInUnitCircle = 90 - camera.getYaw();
        forwardDirectionVector.x = (float) Math.cos(Math.toRadians(forwardAngleInUnitCircle));
        forwardDirectionVector.y = (float) Math.sin(Math.toRadians(forwardAngleInUnitCircle));

        float rightAngleInUnitCircle = -camera.getYaw();
        rightDirectionVector.x = (float) Math.cos(Math.toRadians(rightAngleInUnitCircle));
        rightDirectionVector.y = (float) Math.sin(Math.toRadians(rightAngleInUnitCircle));
    }

}
