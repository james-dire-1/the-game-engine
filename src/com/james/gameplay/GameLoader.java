package com.james.gameplay;

import com.james.audio.objects.AudioListener;
import com.james.common.tools.Mth;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.renderEngine.rendering.ParticleRenderer;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.visuals.LightSettings;
import com.james.serverSide.LevelInitializer;
import com.james.simulation.ClientLevel;
import com.james.simulation.objects.CachedConnectedPlayer;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.Player;
import com.james.tools.*;
import com.james.renderEngine.visuals.Skybox;
import com.james.renderEngine.rendering.SkyboxRenderer;
import org.lwjgl.util.vector.Vector3f;
import templates.communication.ClientPacketSendEvents;

import java.util.Random;

import static org.lwjgl.opengl.GL11.glClearColor;

public abstract class GameLoader {

    private static final float SECONDS_PER_SEND = 0.1f;

    public static Vector3f prop_playerEllipsoidHitboxRadius = new Vector3f(1.0f, 1.0f, 1.0f);
    public static float prop_playerSphereHitboxRadius = 1.0f;
    public static Camera prop_camera = Camera.defaultCamera;
    public static Updater prop_updater;

    public final BatchedGameObjectsList batchedGameObjectsList;
    public final PlayerHandler playerHandler;
    public final LightHandler lightHandler;

    public boolean isPaused = false;
    public Camera camera;

    public GameLoader(ClientPacketSendEvents events, Vector3f spawnPoint) {
        this.batchedGameObjectsList = new BatchedGameObjectsList();

        MasterRenderer.currentCamera = prop_camera;
        this.camera = prop_camera;

        ClientLevel clientLevel = new ClientLevel(events, spawnPoint, prop_playerEllipsoidHitboxRadius, prop_playerSphereHitboxRadius);
        this.playerHandler = new PlayerHandler(this);

        Player player = clientLevel.getPlayer();
        Vector3f playerPosition = player.getPosition();
        float playerRotY = player.getRotation().y;
        clientLevel.events.sendPlayerJoined(playerPosition.x, playerPosition.y, playerPosition.z, playerRotY);

        this.lightHandler = new LightHandler();
        LightSettings.setLightHandler(lightHandler);

        if (prop_updater != null)
            prop_updater.init();
    }

    protected abstract void onGameClientClosing();

    public void update() {
        if (prop_updater != null)
            prop_updater.update(this);

        if (GLFWUtilities.shouldClose) {
            onGameClientClosing();
        }

        if (!isPaused) {
            ClientLevel clientLevel = ClientLevel.get();

            playerHandler.update();
            ParticleHandler.update();

            for (CachedPhysicalObject object : clientLevel.getCachedPhysicalObjects()) {
                Vector3fInterpolator.interpolate(object.getPrevPosition(), object.getPosition(), object.getGameObject().getPosition(), object.lastTime, Time.getCurrentTime());
                Vector3fInterpolator.interpolate(object.getPrevRotation(), object.getRotation(), object.getGameObject().getRotation(), object.lastTime, Time.getCurrentTime());
            }
            for (CachedConnectedPlayer player : clientLevel.getCachedConnectedPlayers()) {
                Vector3fInterpolator.interpolate(player.getPrevPosition(), player.getPosition(), player.getGameObject().getPosition(), player.lastTime, Time.getCurrentTime());
                Vector3fInterpolator.interpolate(player.getPrevRotation(), player.getRotation(), player.getGameObject().getRotation(), player.lastTime, Time.getCurrentTime());
            }

            if (Time.getCurrentTime() - lastTimePlayerPosition >= SECONDS_PER_SEND) {
                lastTimePlayerPosition = Time.getCurrentTime();

                Player player = clientLevel.getPlayer();
                Vector3f playerPosition = player.getPosition();
                float playerRotY = player.getRotation().y;

                clientLevel.events.sendPlayerTransformChanged(playerPosition.x, playerPosition.y, playerPosition.z, playerRotY);
            }

            Vector3f cameraPosition = camera.getPosition();
            Mth.pitchAndYawToGLCartesianCoordinates(1, camera.getPitch(), camera.getYaw(), reusableFacingDirection);
            AudioListener.setPosition(cameraPosition.x, cameraPosition.y, cameraPosition.z);
            AudioListener.setOrientation(reusableFacingDirection.x, reusableFacingDirection.y, reusableFacingDirection.z);
        }
    }

    public void render() {
        if (Skybox.currentSkybox != null) SkyboxRenderer.render(Skybox.currentSkybox);
        MasterRenderer.render(batchedGameObjectsList);
        ParticleRenderer.render(ParticleHandler.particles);

        if (System.nanoTime() / 1000000 - lastTimeBackground > 1000) {
            lastTimeBackground = System.nanoTime() / 1000000;
            glClearColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
        }
    }

    private float lastTimePlayerPosition;
    private long lastTimeBackground;
    private final Random r = new Random();

    private static final Vector3f reusableFacingDirection = new Vector3f();

    public interface Updater {
        void init();
        void update(GameLoader gameLoader);
    }

}
