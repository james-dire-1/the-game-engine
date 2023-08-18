package com.james.main;

import com.james.main.clientSide.CachedObjectHitbox;
import com.james.main.clientSide.ClientCollisionHandler;
import com.james.main.clientSide.ClientPacketReceiveActions;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.gameObjects.Light;
import com.james.input.KeyInput;
import com.james.input.MouseMoveInput;
import com.james.input.WindowResizeInput;
import com.james.math.Mth;
import com.james.physics.SphereHitbox;
import com.james.renderEngine.particles.ComplexParticle;
import com.james.renderEngine.particles.ComplexParticleSettings;
import com.james.renderEngine.particles.Particle;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.particles.dataTypes.FloatType;
import com.james.renderEngine.rendering.*;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.texturing.TextureBank;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.utilities.GLUtilities;
import com.james.tools.*;
import com.james.world.LevelInitializer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {

    // This is some new code that has been added.
    /*
    This
    is a
    test
    let's see
    how it works
     */

    public static final Light light = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

    public static CameraController camController;
    public static BatchedGameObjectsList batchedGameObjectsList;
    public static Camera focusCamera;

    private static final Random r = new Random();
    private static long lastTime;

    private static final float[] positions = {
            -0.5f, -0.5f, 0.0f,      // bottom left vertex
            0.0f,  0.5f, 0.0f,      // top vertex
            0.5f, -0.5f, 0.0f      // bottom right vertex
    };

    private static final int[] indices = {
            1, 0, 2
    };

    private static final float[] textureCoords = {
            0, 0,
            0, 1,
            1, 1
    };

    private static final float[] colors = {
            1, 0, 0, 0, 1, 0, 0, 0, 1
    };

    public static void main(String[] args) {
        TextureBank.init("/hello.png", "/stall.png", "/rowdies.png", "/arial.png", "/GameInventorySlot.png", "/cobblestone_wall.png");
        GLFWUtilities.init();
        ParticleHandler.init();

        Renderers.texturedModelRenderer = new TexturedModelRenderer();
        Renderers.colorModelRenderer = new ColorModelRenderer();
        Renderers.flatRenderer = new FlatRenderer();

        FontInfo arial = new FontInfo("/arial.fnt", "/arial.png");
        FontInfo rowdies = new FontInfo("/rowdies.fnt", "/rowdies.png");

        List<Gui> guis = new ArrayList<>();
        GuiText guiText = new GuiText("Yoooooo!", rowdies, 1, GuiText.TextAlignment.CENTER_ALIGNED, guis,
            new ScreenPosition(100, 100));
        Gui gui = new Gui(new ScreenPosition(100, 500), new ScreenSize(100, 20), null);
        gui.setTextureAndSamplingData("/stall.png");
        gui.apply();
        Gui gui2 = new Gui(new ScreenPosition(WindowResizeInput.width/2, WindowResizeInput.height/2), new ScreenSize(100, 20), null);
        gui2.setTextureAndSamplingData("/rowdies.png");
        gui2.apply();
        guis.add(gui);

        Gui gui3 = new Gui(new AnchoredPosition(AnchorPoint.CENTER), new ScreenSize(100, 100), null);
        gui3.setTextureAndSamplingData("/GameInventorySlot.png");
        gui3.apply();
        guis.add(gui3);

        ScreenTest screenTest = new ScreenTest();
        UiHandler.screens.add(screenTest);

        GameObject stallGameObject = new GameObject(ModelBank.getStall(), new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 1);
        GameObject abstractGameObject = new GameObject(ModelBank.getAbstractArt(), new Vector3f(0, 5, -5), new Vector3f(0, 0, 0), 1);

        batchedGameObjectsList = new BatchedGameObjectsList();
        batchedGameObjectsList.addGameObject(stallGameObject);
        batchedGameObjectsList.addGameObject(abstractGameObject);

        Camera.defaultCamera.setPosition(new Vector3f(0, 0, 5));
        SphereHitbox hitbox = new SphereHitbox(abstractGameObject.getPosition(), 3);

        MasterRenderer.prepare(Renderers.colorModelRenderer, Renderers.texturedModelRenderer, Renderers.flatRenderer);
        ParticleRenderer.prepare();

        GLFWUtilities.lockCursor(true);

        long lastTime2 = System.nanoTime()/1000000;
        int fps = 0;

        boolean isGamepad = glfwJoystickIsGamepad(GLFW_JOYSTICK_1);

        float distance = 5;

        focusCamera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);

        LocalServerPacketSendEvents localLevelEvents = new LocalServerPacketSendEvents();
        LevelInitializer levelInitializer = new LevelInitializer(localLevelEvents);

        ClientPacketReceiveActions.player = new Player(new Vector3f(-5, 0, 0));
        ClientPacketReceiveActions.playerHitbox = new PlayerHitbox(ClientPacketReceiveActions.player, 1);

        while (!GLFWUtilities.shouldClose) {
            Time.updateDeltaTime();

            // key input
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
            if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_Q)) {
                distance -= 0.1f;
            }
            if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_E)) {
                distance += 0.1f;
            }

            // mouse picker
            MousePicker.update();

            abstractGameObject.setPosition(Vector3f.add(Camera.defaultCamera.getPosition(), Mth.multiply(MousePicker.getCurrentRay(), distance), null));

            if (KeyInput.isKeyDown(GLFW.GLFW_KEY_Y)) {
                gui.size = new ScreenSize(new Random().nextInt(100), new Random().nextInt(100));
            }

            if (KeyInput.isKeyDown(GLFW_KEY_5)) {
                //new Particle(new Vector3f(0, 10, -5), new Vector3f(0, 0, 0), 0, 1, 1, 3);
                ComplexParticleSettings settings = new ComplexParticleSettings();
                settings.addTargetKeyframe(0, FloatType.GravityMultiplier, 0);
                settings.addTargetKeyframe(10, FloatType.Rotation, 360);
                new ComplexParticle(10, settings);
            }

            if (KeyInput.isKeyDown(GLFW_KEY_6)) {
                new Particle(new Vector3f(0, 10, 0), new Vector3f(0, -10f, 0), 0, 1, 1, 10);
            }

            if (KeyInput.isKeyPressed(GLFW_KEY_7)) {
                ((ScreenPosition) guiText.master.position).changePosition(1, 1);
            }
            if (KeyInput.isKeyPressed(GLFW_KEY_8)) {
                ((ScreenPosition) guiText.master.position).setPosition(WindowResizeInput.width/2, WindowResizeInput.height/2);
            }

            if (KeyInput.isKeyDown(GLFW_KEY_BACKSLASH)) {
                if (MasterRenderer.currentCamera.equals(Camera.defaultCamera)) {
                    MasterRenderer.currentCamera = focusCamera;
                } else if (MasterRenderer.currentCamera.equals(focusCamera)) {
                    MasterRenderer.currentCamera = Camera.defaultCamera;
                }
            }

            // controller stuff
            if (isGamepad) {
                GLFWGamepadState state = GLFWGamepadState.create();
                glfwGetGamepadState(GLFW_JOYSTICK_1, state);

                float normalizedPositionX = Math.abs(state.axes(GLFW_GAMEPAD_AXIS_LEFT_X)) > 0.1f ? state.axes(GLFW_GAMEPAD_AXIS_LEFT_X) : 0;
                float normalizedPositionY = state.buttons(GLFW_GAMEPAD_BUTTON_CROSS) + state.buttons(GLFW_GAMEPAD_BUTTON_CIRCLE) * -1;
                float normalizedPositionZ = Math.abs(state.axes(GLFW_GAMEPAD_AXIS_LEFT_Y)) > 0.1f ? state.axes(GLFW_GAMEPAD_AXIS_LEFT_Y) : 0;

                Camera.defaultCamera.translate(new Vector3f(normalizedPositionX, normalizedPositionY, normalizedPositionZ));

                float normalizedPitch = state.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y);
                float normalizedYaw = state.axes(GLFW_GAMEPAD_AXIS_RIGHT_X);
                float normalizedRoll = state.axes(GLFW_GAMEPAD_AXIS_LEFT_TRIGGER) - state.axes(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);
                Camera.defaultCamera.increasePitch(Math.abs(normalizedPitch) > 0.1f ? normalizedPitch * 3f : 0);
                Camera.defaultCamera.increaseYaw(Math.abs(normalizedYaw) > 0.1f ? normalizedYaw * 3f : 0);
                Camera.defaultCamera.setRoll(normalizedRoll * 45);
            }

            // important stuff
            GLFWUtilities.pollEvents();
            ParticleHandler.update();
            UiHandler.update();
            ClientPacketReceiveActions.player.update();
            ClientCollisionHandler.update();

            // camera controller
            if (camController != null)
                camController.update();

            // rendering
            MasterRenderer.render(batchedGameObjectsList);
            ParticleRenderer.render(ParticleHandler.particles);
            //GuiRenderer.render(guis);
            GuiRenderer.render(UiHandler.guisToRender);
            GLFWUtilities.render();

            if (GLFWUtilities.shouldClose) {
                levelInitializer.shouldRun = false;
            }

            // fps timer
            fps++;
            if (System.nanoTime()/1000000 - lastTime2 > 1000) {
                lastTime2 = System.nanoTime()/1000000;
                System.out.println("FPS: " + fps);
                fps = 0;
            }

            // rotating game objects
//            for (List<GameObject> gameObjectsList : batchedGameObjectsList.getGameObjectsMap().values())
//                for (GameObject gameObject : gameObjectsList)
//                    gameObject.rotate(new Vector3f(0, 1, 0));

            // color changing background
            if (System.nanoTime()/1000000 - lastTime > 1000) {
                lastTime = System.nanoTime()/1000000;
                glClearColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
            }

            ThreadManager.updateMain();
        }

        MasterRenderer.cleanUp();
        ParticleRenderer.cleanUp();
        GuiRenderer.cleanUp();
        GLUtilities.cleanUp();
        GLFWUtilities.cleanUp();
    }

}
