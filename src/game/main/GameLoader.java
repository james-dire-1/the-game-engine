package game.main;

import com.james.common.simulation.objects.PhysicalObjectType;
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
import com.james.serverSide.LevelInitializer;
import com.james.serverSide.simulation.collisionEngine.hitboxes.AABBHitbox;
import com.james.serverSide.simulation.objects.PhysicalObject;
import com.james.tools.BatchedGameObjectsList;
import com.james.tools.MousePicker;
import game.communication.ClientPacketReceiveActions;
import game.communication.LocalServerPacketSendEvents;
import game.player.PlayerHandler;
import newStuff.ClientLevel;
import newStuff.LocalClientPacketSendEvents;
import newStuff.LocalServerProperties;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class GameLoader {

    public static Camera focusCamera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);
    // TODO: 2024-06-27 Make this possibly non-static in the future?
    public static BatchedGameObjectsList batchedGameObjectsList;
    public static boolean serverIsReady = false;

    private final LevelInitializer levelInitializer;
    private final LocalServerProperties properties;
    private final ClientLevel clientLevel;
    private final PlayerHandler playerHandler;

    private boolean firstTime = true;

    private static long lastTime;
    private static final Random r = new Random();

    public GameLoader()  {
        batchedGameObjectsList = new BatchedGameObjectsList();

        this.levelInitializer = new LevelInitializer(new LocalServerPacketSendEvents()) {
            @Override
            public void onStartup() {
                for (int i = 0; i < 10; i++) {
                    PhysicalObject wall = new PhysicalObject(PhysicalObjectType.Wall, new Vector3f(-i*2, 0, -10), new Vector3f(0, 0, 0), 1);
                    level.add(wall);
                    level.events.sendPhysicalObjectAddedToLevel(wall.id, wall.type, wall.getPosition(), wall.getRotation(), wall.getScale());
                }

                PhysicalObject testEnvironment = new PhysicalObject(PhysicalObjectType.TestEnvironment, new Vector3f(), new Vector3f(), 1);
                level.add(testEnvironment);
                level.events.sendPhysicalObjectAddedToLevel(testEnvironment.id, testEnvironment.type, testEnvironment.getPosition(), testEnvironment.getRotation(), testEnvironment.getScale());

                AABBHitbox aabbHitbox = new AABBHitbox(testEnvironment, "res/test_environment_7.dae");
                level.addAABBHitbox(aabbHitbox);
                level.events.sendAABBHitboxAdded(testEnvironment.id, aabbHitbox.meshPath);
            }
        };

        this.properties = new LocalServerProperties();
        this.clientLevel = new ClientLevel(new LocalClientPacketSendEvents());

        this.playerHandler = new PlayerHandler(batchedGameObjectsList, ClientPacketReceiveActions.levelProperties, new Vector3f(-5, 25, 0), focusCamera);
        ClientPacketReceiveActions.levelProperties.setTicksPerSecond(40);

        Camera.defaultCamera.setPosition(new Vector3f(0, 0, 5));
        GLFWUtilities.lockCursor(true);
    }

    public void update() {
        // mouse picker
        MousePicker.update();

        if (firstTime) {
            firstTime = false;
            Vector3f playerPosition = ClientPacketReceiveActions.player.getPosition();
            clientLevel.events.sendPlayerJoined(properties, playerPosition.x, playerPosition.y, playerPosition.z);
        } else {

        }

        input();
        playerHandler.update();
        ParticleHandler.update();

        if (GLFWUtilities.shouldClose) {
            levelInitializer.shouldRun = false;
        }
    }

    public void render() {
        MasterRenderer.render(batchedGameObjectsList);
        ParticleRenderer.render(ParticleHandler.particles);

        // color changing background
        if (System.nanoTime()/1000000 - lastTime > 1000) {
            lastTime = System.nanoTime()/1000000;
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
                MasterRenderer.currentCamera = GameLoader.focusCamera;
            } else if (MasterRenderer.currentCamera.equals(GameLoader.focusCamera)) {
                MasterRenderer.currentCamera = Camera.defaultCamera;
            }
        }
    }

}
