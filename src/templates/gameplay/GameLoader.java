package templates.gameplay;

import com.james.input.KeyInput;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.DirectionalLight;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.renderEngine.rendering.ParticleRenderer;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.visuals.FogSettings;
import com.james.renderEngine.visuals.LightSettings;
import com.james.simulation.ClientLevel;
import com.james.simulation.objects.CachedConnectedPlayer;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.Player;
import com.james.tools.BatchedGameObjectsList;
import com.james.tools.Time;
import game.ui.screens.PauseScreen;
import com.james.tools.GameObjectInterpolator;
import com.james.renderEngine.visuals.Skybox;
import com.james.renderEngine.rendering.SkyboxRenderer;
import game.ui.screens.DebugScreen;
import com.james.tools.LightHandler;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import templates.communication.ClientPacketSendEvents;
import templates.rendering.ModelBank;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public abstract class GameLoader {

    public static Camera focusCamera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);
    public static BatchedGameObjectsList batchedGameObjectsList;
    public static LightHandler lightHandler;

    protected final ClientLevel clientLevel;
    protected final PlayerHandler playerHandler;

    private long lastTimeBackground;
    private float lastTimePlayerPosition;
    private final Random r = new Random();
    private boolean prevIsOpen;

    private static final float SECONDS_PER_SEND = 0.1f;

    private final float referenceTime;

    public GameLoader(ClientPacketSendEvents events) {
        batchedGameObjectsList = new BatchedGameObjectsList();
        Camera.defaultCamera.setPosition(new Vector3f(0, 0, 5));

        this.clientLevel = new ClientLevel(events, new Vector3f(-5, 8, 0), new Vector3f(1, 1, 1));
        this.playerHandler = new PlayerHandler(batchedGameObjectsList, this.clientLevel, focusCamera);

        Player player = ClientLevel.get().getPlayer();
        Vector3f playerPosition = player.getPosition();
        float rotY = player.getRotation().y;
        clientLevel.events.sendPlayerJoined(playerPosition.x, playerPosition.y, playerPosition.z, rotY);

        MasterRenderer.currentCamera = focusCamera;

        FogSettings.setUseSphericalFog(FogSettings.DEFAULT_USE_SPHERICAL_FOG);
        FogSettings.setFogDensity(FogSettings.DEFAULT_DENSITY);
        FogSettings.setFogGradient(FogSettings.DEFAULT_GRADIENT);

        lightHandler = new LightHandler();
        LightSettings.setLightHandler(lightHandler);
//        lightHandler.addDirectionalLight(1, new DirectionalLight(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));

        Vector3f sharedPosition1 = new Vector3f(-50, -5, -60);
        lightHandler.addLight(0, new Light(sharedPosition1, new Vector3f(1, 0, 1), Light.DEFAULT_ATTENUATION));
        batchedGameObjectsList.addGameObject(new GameObject(ModelBank.getLightSource(), sharedPosition1));

        Vector3f sharedPosition2 = new Vector3f(25, 5, 80);
        lightHandler.addLight(1, new Light(sharedPosition2, new Vector3f(0, 1, 0), Light.DEFAULT_ATTENUATION));
        batchedGameObjectsList.addGameObject(new GameObject(ModelBank.getLightSource(), sharedPosition2));

        Vector3f sharedPosition3 = new Vector3f(30, 10, -50);
        lightHandler.addLight(2, new Light(sharedPosition3, new Vector3f(1, 0.5f, 0), Light.DEFAULT_ATTENUATION));
        batchedGameObjectsList.addGameObject(new GameObject(ModelBank.getLightSource(), sharedPosition3));

        Vector3f sharedPosition4 = new Vector3f(-20, 15, 5);
        lightHandler.addLight(3, new Light(sharedPosition4, new Vector3f(1, 1, 0), Light.DEFAULT_ATTENUATION));
        batchedGameObjectsList.addGameObject(new GameObject(ModelBank.getLightSource(), sharedPosition4));

        this.referenceTime = Time.getCurrentTime();
    }

    protected abstract void onGameClientClosing();

    public void update() {
        input();
        playerHandler.update();
        ParticleHandler.update();

        for (CachedPhysicalObject object : clientLevel.getCachedPhysicalObjects()) {
            GameObjectInterpolator.interpolate(object.getPrevPosition(), object.getPosition(), object.getGameObject().getPosition(), object.lastTime, Time.getCurrentTime());
            GameObjectInterpolator.interpolate(object.getPrevRotation(), object.getRotation(), object.getGameObject().getRotation(), object.lastTime, Time.getCurrentTime());
        }
        for (CachedConnectedPlayer player : clientLevel.getCachedConnectedPlayers()) {
            GameObjectInterpolator.interpolate(player.getPrevPosition(), player.getPosition(), player.getGameObject().getPosition(), player.lastTime, Time.getCurrentTime());
            GameObjectInterpolator.interpolate(player.getPrevRotation(), player.getRotation(), player.getGameObject().getRotation(), player.lastTime, Time.getCurrentTime());
        }

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

//        float normalizedTimeOfDay = ((Time.getCurrentTime() - referenceTime) % 1200) / 1200;
//        float xDirection = (float) Math.cos(normalizedTimeOfDay * 2 * Math.PI + Math.toRadians(80));
//        float yDirection = (float) Math.sin(normalizedTimeOfDay * 2 * Math.PI + Math.toRadians(80));
//        lightHandler.getDirectionalLight(1).setToLightDirection(xDirection, yDirection, 0);
    }

    public void render() {
        if (Skybox.currentSkybox != null) SkyboxRenderer.render(Skybox.currentSkybox);
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

        if (KeyInput.isKeyDown(GLFW_KEY_TAB)) {
            PauseScreen.toggle();
        }

        if (KeyInput.isKeyDown(GLFW_KEY_F3)) {
            DebugScreen.toggle();
        }

        if (KeyInput.isKeyPressed(GLFW_KEY_F)) {
            playerHandler.getCamController().firstPerson = false;
            playerHandler.getGameObject().isVisible = true;
        } else {
            playerHandler.getCamController().firstPerson = true;
            playerHandler.getGameObject().isVisible = false;
        }

        float deltaX = 0;
        if (KeyInput.isKeyPressed(GLFW_KEY_RIGHT)) {
            deltaX += 0.2f;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_LEFT)) {
            deltaX -= 0.2f;
        }

        float deltaZ = 0;
        if (KeyInput.isKeyPressed(GLFW_KEY_UP)) {
            deltaZ -= 0.2f;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_DOWN)) {
            deltaZ += 0.2f;
        }

        float deltaY = 0;
        if (KeyInput.isKeyPressed(GLFW_KEY_O)) {
            deltaY += 0.2f;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_P)) {
            deltaY -= 0.2f;
        }

        if (deltaX != 0 || deltaY != 0 || deltaZ != 0) {
            lightHandler.getLight(currentLight).getPosition().translate(deltaX, deltaY, deltaZ);
        }

        if (KeyInput.isKeyDown(GLFW_KEY_U)) {
            state = !state;
            Vector3f attenuation;
            if (state) {
                attenuation = Light.NO_ATTENUATION;
            } else {
                attenuation = Light.DEFAULT_ATTENUATION;
            }
            lightHandler.getLight(currentLight).setAttenuation(attenuation.x, attenuation.y, attenuation.z);
        }

        if (KeyInput.isKeyDown(GLFW_KEY_Y)) {
            currentLight = (currentLight + 1) % 4;
        }
    }

    int currentLight;
    boolean state;

}
