package game.player;

import com.james.common.simulation.LevelProperties;
import com.james.simulation.objects.Player;
import com.james.tools.BatchedGameObjectsList;
import com.james.tools.CameraController;
import com.james.common.tools.Mth;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.tools.Time;
import newStuff.GameObjectInterpolator;
import org.lwjgl.util.vector.Vector3f;
import templates.rendering.ModelBank;
import com.james.simulation.ClientLevel;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static com.james.input.KeyInput.isKeyPressed;

/**
 * Client-side class which handles much of the player logic, including aspects regarding simulation,
 * user input, camera movement, and rendering (i.e. it creates the Player's GameObject).
 */
public class PlayerHandler {

    private final Player player;
    private final LevelProperties levelProperties;
    private final CameraController camController;
    private final GameObject gameObject;

    private final Vector2f forwardDirectionVector = new Vector2f();
    private final Vector2f rightDirectionVector = new Vector2f();

    private static final float SPEED = 15;

    /**
     * Gets the Player, creates its GameObject and its CameraController.
     */
    public PlayerHandler(BatchedGameObjectsList batchedGameObjectsList, LevelProperties levelProperties, Camera camera) {
        this.player = ClientLevel.get().getPlayer();

        Model model = ModelBank.getAbstractArt();
        this.gameObject = new GameObject(model, new Vector3f(player.getPosition()), player.getRotation(), 1);
        batchedGameObjectsList.addGameObject(gameObject);

        this.levelProperties = levelProperties;
        this.camController = new CameraController(camera, gameObject.getPosition(), 20);

        GameObjectInterpolator.secondsPerGameTick = levelProperties.secondsPerGameTick;
    }

    /**
     * Updates player rotation, which happens every frame, simulation logic, which happens every
     * game tick, and the camera controller, which happens every frame. During the simulation update,
     * sets the velocity of the player based on the direction vectors and user input, and updates client-side
     * collisions.
     */
    public void update() {
        Camera camera = camController.getCamera();

        player.getRotation().y = -camera.getYaw();

        if (Time.getCurrentTime() - player.lastTime >= levelProperties.secondsPerGameTick) {
            player.updatePrevPosition();

            player.lastTime = Time.getCurrentTime();
            calculateDirectionVectors();

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

            Vector2f velocity = Vector2f.add(Mth.multiply(forwardDirectionVector, forwardSpeed), Mth.multiply(rightDirectionVector, rightSpeed), null);
            player.setVelocity(velocity.x, 0, -velocity.y);

            ClientLevel.get().update();
        }

        GameObjectInterpolator.interpolate(player.getPrevPosition(), player.getPosition(), gameObject.getPosition(), player.lastTime, Time.getCurrentTime());

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

}
