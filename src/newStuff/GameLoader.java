package newStuff;

import com.james.input.KeyInput;
import com.james.input.MouseMoveInput;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.particles.ComplexParticle;
import com.james.renderEngine.particles.ComplexParticleSettings;
import com.james.renderEngine.particles.Particle;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.particles.dataTypes.FloatType;
import com.james.renderEngine.rendering.MasterRenderer;
import com.james.renderEngine.rendering.ParticleRenderer;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.simulation.ClientLevel;
import com.james.simulation.objects.Player;
import com.james.tools.BatchedGameObjectsList;
import com.james.tools.MousePicker;
import com.james.tools.Time;
import game.main.LocalGameLoader;
import game.player.PlayerHandler;
import game.ui.screens.PauseScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import templates.communication.ClientPacketSendEvents;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public abstract class GameLoader {

    public static Camera focusCamera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);
    public static BatchedGameObjectsList batchedGameObjectsList;

    protected final ClientLevel clientLevel;
    private final PlayerHandler playerHandler;

    private long lastTimeBackground;
    private float lastTimePlayerPosition;
    private final Random r = new Random();
    private boolean prevIsOpen;

    private static final float SECONDS_PER_SEND = 0.1f;

    public GameLoader(ClientPacketSendEvents events) {
        additionalStartupActions();

        batchedGameObjectsList = new BatchedGameObjectsList();
        Camera.defaultCamera.setPosition(new Vector3f(0, 0, 5));

        this.clientLevel = new ClientLevel(events, new Vector3f(-5, 25, 0));
        this.playerHandler = new PlayerHandler(batchedGameObjectsList, this.clientLevel, focusCamera);

        Player player = ClientLevel.get().getPlayer();
        Vector3f playerPosition = player.getPosition();
        float rotY = player.getRotation().y;
        clientLevel.events.sendPlayerJoined(playerPosition.x, playerPosition.y, playerPosition.z, rotY);
    }

    protected abstract void additionalStartupActions();
    protected abstract void onGameClientClosing();

    public void update() {
        MousePicker.update();

        input();
        playerHandler.update();
        ParticleHandler.update();

        if (Time.getCurrentTime() - lastTimePlayerPosition >= SECONDS_PER_SEND) {
            lastTimePlayerPosition = Time.getCurrentTime();

            Player player = ClientLevel.get().getPlayer();
            Vector3f playerPosition = player.getPosition();
            float rotY = player.getRotation().y;


            clientLevel.events.sendPlayerTransformChanged(playerPosition.x, playerPosition.y, playerPosition.z, rotY);
        }

        if (PauseScreen.isOpen != prevIsOpen) {
            prevIsOpen = PauseScreen.isOpen;
            clientLevel.events.sendChangePauseState(PauseScreen.isOpen);
        }

        if (GLFWUtilities.shouldClose) {
            onGameClientClosing();
        }
    }

    public void render() {
        MasterRenderer.render(batchedGameObjectsList);
        ParticleRenderer.render(ParticleHandler.particles);

        // color changing background
        if (System.nanoTime()/1000000 - lastTimeBackground > 1000) {
            lastTimeBackground = System.nanoTime()/1000000;
            glClearColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
        }
    }

    private void input() {
        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_R)) {
            GLFWUtilities.lockCursor(!GLFWUtilities.isCursorLocked());
        }

        if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_W)) {
            Camera.defaultCamera.translate(new Vector3f(0, 0, -.1f));
        }
        if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_S)) {
            Camera.defaultCamera.translate(new Vector3f(0, 0, .1f));
        }
        if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_A)) {
            Camera.defaultCamera.translate(new Vector3f(-.1f, 0, 0));
        }
        if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_D)) {
            Camera.defaultCamera.translate(new Vector3f(.1f, 0, 0));
        }

        if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            Camera.defaultCamera.increasePitch((float) (0.2 * MouseMoveInput.getDeltaY()));
            Camera.defaultCamera.increaseYaw((float) (0.2 * MouseMoveInput.getDeltaX()));
        }

        if (KeyInput.isKeyDown(GLFW_KEY_5)) {
            ComplexParticleSettings settings = new ComplexParticleSettings();
            settings.addTargetKeyframe(0, FloatType.GravityMultiplier, 0);
            settings.addTargetKeyframe(10, FloatType.Rotation, 360);
            new ComplexParticle(10, settings);
        }

        if (KeyInput.isKeyDown(GLFW_KEY_6)) {
            new Particle(new Vector3f(0, 10, 0), new Vector3f(0, -10f, 0), 0, 1, 1, 10);
        }

        if (KeyInput.isKeyDown(GLFW_KEY_BACKSLASH)) {
            if (MasterRenderer.currentCamera.equals(Camera.defaultCamera)) {
                MasterRenderer.currentCamera = LocalGameLoader.focusCamera;
            } else if (MasterRenderer.currentCamera.equals(LocalGameLoader.focusCamera)) {
                MasterRenderer.currentCamera = Camera.defaultCamera;
            }
        }

        if (KeyInput.isKeyDown(GLFW_KEY_TAB)) {
            PauseScreen.toggle();
        }
    }

}
