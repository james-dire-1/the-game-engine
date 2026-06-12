package game.ui.screens;

import com.james.input.KeyInput;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.renderEngine.textRendering.TextAlignment;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.simulation.ClientLevel;
import com.james.tools.Time;
import game.main.Main;
import com.james.renderEngine.visuals.FogSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static com.james.common.simulation.collisionEngine.math.CommonCollisionProcedure.DebugAccumulator;

public class DebugScreen extends Screen {

    private static final float REFRESH_TIME = 0.1f;

    public static DebugAccumulator collisionsAccumulator = new DebugAccumulator();

    private static boolean isOpen = false;
    private static DebugScreen currentInstance;

    private final PersistentGuiText gameNameDisplay;
    private final PersistentGuiText howToCloseDisplay;
    private final PersistentGuiText cannotCloseDisplay;
    private final GuiText fpsDisplay;
    private final GuiText frameTimeDisplay;
    private final PersistentGuiText drawCallsDisplayLeft;
    private final GuiText drawCallsDisplayRight;
    private final PersistentGuiText trianglesDisplayLeft;
    private final GuiText trianglesDisplayRight;
    private final PersistentGuiText aabbCollisionsDisplayLeft;
    private final GuiText aabbCollisionsDisplayRight;
    private final GuiText playerPositionDisplay;
    private final GuiText cameraOrientationDisplay;
    private final GuiText fogDensityDisplay;
    private final GuiText fogGradientDisplay;
    private final GuiText fogTypeDisplay;
    private final PersistentGuiText javaVersionDisplay;
    private final PersistentGuiText openglVersionDisplay;
    private final PersistentGuiText gpuDisplay;
    private final GuiText windowDimensionsDisplay;
    private final PersistentGuiText usedMemoryDisplayLeft;
    private final GuiText usedMemoryDisplayRight;
    private final GuiText allocatedMemoryDisplay;

    private int lastFps;
    private float lastFrameTime;
    private int lastDrawCalls;
    private int lastTriangles;
    private int lastAabbCollisions;
    private final Vector3f lastPlayerPosition;
    private final Vector3f lastCameraOrientation;
    private boolean lastFogType;
    private float lastFogDensity;
    private float lastFogGradient;
    private int lastWindowWidth;
    private int lastWindowHeight;
    private long lastUsedMemory;
    private long lastAllocatedMemory;

    private boolean lastInGame;
    private float lastDisplayTime;
    private Mode mode = Mode.Everything;
    private Mode lastMode = mode;

    public DebugScreen() {
        this.lastPlayerPosition = new Vector3f();
        this.lastCameraOrientation = new Vector3f();

        this.gameNameDisplay = new PersistentGuiText("Survival Game", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, 0)));
        gameNameDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        gameNameDisplay.apply();
        gameNameDisplay.setPriority(2);
        super.addGui(gameNameDisplay.getMesh());

        this.howToCloseDisplay = new PersistentGuiText("F3 to close this menu", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -25)));
        howToCloseDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        howToCloseDisplay.apply();
        howToCloseDisplay.setPriority(2);
        super.addGui(howToCloseDisplay.getMesh());

        this.cannotCloseDisplay = new PersistentGuiText("Can't close debug menu now", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -25)));
        cannotCloseDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        cannotCloseDisplay.apply();
        cannotCloseDisplay.setPriority(2);
        cannotCloseDisplay.setVisibility(false);
        super.addGui(cannotCloseDisplay.getMesh());

        this.fpsDisplay = new GuiText("FPS: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -50)));
        fpsDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fpsDisplay.makeEditable(this, 0);
        fpsDisplay.apply();
        fpsDisplay.setPriority(2);
        super.addGuis(fpsDisplay.getAllGuis());

        this.frameTimeDisplay = new GuiText(" - Frame time: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(80, -50)));
        frameTimeDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        frameTimeDisplay.makeEditable(this, 0);
        frameTimeDisplay.apply();
        frameTimeDisplay.setPriority(2);
        super.addGuis(frameTimeDisplay.getAllGuis());

        this.drawCallsDisplayLeft = new PersistentGuiText("Draw calls: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -100)));
        drawCallsDisplayLeft.setAlignment(TextAlignment.LEFT_ALIGNED);
        drawCallsDisplayLeft.apply();
        drawCallsDisplayLeft.setPriority(2);
        super.addGui(drawCallsDisplayLeft.getMesh());

        this.drawCallsDisplayRight = new GuiText("", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(160, -100)));
        drawCallsDisplayRight.setAlignment(TextAlignment.LEFT_ALIGNED);
        drawCallsDisplayRight.makeEditable(this, 0);
        drawCallsDisplayRight.apply();
        drawCallsDisplayRight.setPriority(2);
        super.addGuis(drawCallsDisplayRight.getAllGuis());

        this.trianglesDisplayLeft = new PersistentGuiText("Triangles: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -125)));
        trianglesDisplayLeft.setAlignment(TextAlignment.LEFT_ALIGNED);
        trianglesDisplayLeft.apply();
        trianglesDisplayLeft.setPriority(2);
        super.addGui(trianglesDisplayLeft.getMesh());

        this.trianglesDisplayRight = new GuiText("", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(160, -125)));
        trianglesDisplayRight.setAlignment(TextAlignment.LEFT_ALIGNED);
        trianglesDisplayRight.makeEditable(this, 0);
        trianglesDisplayRight.apply();
        trianglesDisplayRight.setPriority(2);
        super.addGuis(trianglesDisplayRight.getAllGuis());

        this.aabbCollisionsDisplayLeft = new PersistentGuiText("AABB collisions: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -150)));
        aabbCollisionsDisplayLeft.setAlignment(TextAlignment.LEFT_ALIGNED);
        aabbCollisionsDisplayLeft.apply();
        aabbCollisionsDisplayLeft.setPriority(2);
        super.addGui(aabbCollisionsDisplayLeft.getMesh());

        this.aabbCollisionsDisplayRight = new GuiText("", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(160, -150)));
        aabbCollisionsDisplayRight.setAlignment(TextAlignment.LEFT_ALIGNED);
        aabbCollisionsDisplayRight.makeEditable(this, 0);
        aabbCollisionsDisplayRight.apply();
        aabbCollisionsDisplayRight.setPriority(2);
        super.addGuis(aabbCollisionsDisplayRight.getAllGuis());

        this.playerPositionDisplay = new GuiText("(x: , y: , z: )", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -200)));
        playerPositionDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        playerPositionDisplay.makeEditable(this, 0);
        playerPositionDisplay.apply();
        playerPositionDisplay.setPriority(2);
        super.addGuis(playerPositionDisplay.getAllGuis());

        this.cameraOrientationDisplay = new GuiText("pitch: , yaw: , roll: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -225)));
        cameraOrientationDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        cameraOrientationDisplay.makeEditable(this, 0);
        cameraOrientationDisplay.apply();
        cameraOrientationDisplay.setPriority(2);
        super.addGuis(cameraOrientationDisplay.getAllGuis());

        this.fogTypeDisplay = new GuiText("Fog type: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -275)));
        fogTypeDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogTypeDisplay.makeEditable(this, 0);
        fogTypeDisplay.apply();
        fogTypeDisplay.setPriority(2);
        super.addGuis(fogTypeDisplay.getAllGuis());

        this.fogDensityDisplay = new GuiText("Fog density: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -300)));
        fogDensityDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogDensityDisplay.makeEditable(this, 0);
        fogDensityDisplay.apply();
        fogDensityDisplay.setPriority(2);
        super.addGuis(fogDensityDisplay.getAllGuis());

        this.fogGradientDisplay = new GuiText("Fog gradient: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -325)));
        fogGradientDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogGradientDisplay.makeEditable(this, 0);
        fogGradientDisplay.apply();
        fogGradientDisplay.setPriority(2);
        super.addGuis(fogGradientDisplay.getAllGuis());

        this.javaVersionDisplay = new PersistentGuiText("Java " + System.getProperty("java.version"), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, 0)));
        javaVersionDisplay.setAlignment(TextAlignment.RIGHT_ALIGNED);
        javaVersionDisplay.apply();
        javaVersionDisplay.setPriority(2);
        super.addGui(javaVersionDisplay.getMesh());

        this.openglVersionDisplay = new PersistentGuiText("OpenGL " + GL30.glGetString(GL30.GL_VERSION), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -25)));
        openglVersionDisplay.setAlignment(TextAlignment.RIGHT_ALIGNED);
        openglVersionDisplay.apply();
        openglVersionDisplay.setPriority(2);
        super.addGui(openglVersionDisplay.getMesh());

        this.gpuDisplay = new PersistentGuiText(GL11.glGetString(GL11.GL_RENDERER), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -50)));
        gpuDisplay.setAlignment(TextAlignment.RIGHT_ALIGNED);
        gpuDisplay.apply();
        gpuDisplay.setPriority(2);
        super.addGui(gpuDisplay.getMesh());

        this.windowDimensionsDisplay = new GuiText("", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -100)));
        windowDimensionsDisplay.setAlignment(TextAlignment.RIGHT_ALIGNED);
        windowDimensionsDisplay.makeEditable(this, 0);
        windowDimensionsDisplay.apply();
        windowDimensionsDisplay.setPriority(2);
        super.addGuis(windowDimensionsDisplay.getAllGuis());

        this.usedMemoryDisplayLeft = new PersistentGuiText("Memory used: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-180, -125)));
        usedMemoryDisplayLeft.setAlignment(TextAlignment.RIGHT_ALIGNED);
        usedMemoryDisplayLeft.apply();
        usedMemoryDisplayLeft.setPriority(2);
        super.addGui(usedMemoryDisplayLeft.getMesh());

        this.usedMemoryDisplayRight = new GuiText("", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -125)));
        usedMemoryDisplayRight.setAlignment(TextAlignment.RIGHT_ALIGNED);
        usedMemoryDisplayRight.makeEditable(this, 0);
        usedMemoryDisplayRight.apply();
        usedMemoryDisplayRight.setPriority(2);
        super.addGuis(usedMemoryDisplayRight.getAllGuis());

        this.allocatedMemoryDisplay = new GuiText("Allocated: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -150)));
        allocatedMemoryDisplay.setAlignment(TextAlignment.RIGHT_ALIGNED);
        allocatedMemoryDisplay.makeEditable(this, 0);
        allocatedMemoryDisplay.apply();
        allocatedMemoryDisplay.setPriority(2);
        super.addGuis(allocatedMemoryDisplay.getAllGuis());
    }

    @Override
    public void update() {
        boolean inGame = ClientLevel.get() != null;

        if (lastInGame != inGame || lastMode != mode) {
            lastInGame = inGame;
            lastMode = mode;

            gameNameDisplay.setVisibility(mode == Mode.Everything);
            howToCloseDisplay.setVisibility(inGame && mode == Mode.Everything);
            cannotCloseDisplay.setVisibility(!inGame && mode == Mode.Everything);
            fpsDisplay.setVisibility(mode == Mode.Everything || mode == Mode.FPS);
            frameTimeDisplay.setVisibility(mode == Mode.Everything);
            drawCallsDisplayLeft.setVisibility(mode == Mode.Everything || mode == Mode.DrawCalls);
            drawCallsDisplayRight.setVisibility(mode == Mode.Everything || mode == Mode.DrawCalls);
            trianglesDisplayLeft.setVisibility(mode == Mode.Everything || mode == Mode.Triangles);
            trianglesDisplayRight.setVisibility(mode == Mode.Everything || mode == Mode.Triangles);
            aabbCollisionsDisplayLeft.setVisibility(inGame && mode == Mode.Everything);
            aabbCollisionsDisplayRight.setVisibility(inGame && mode == Mode.Everything);
            playerPositionDisplay.setVisibility(inGame && mode == Mode.Everything);
            cameraOrientationDisplay.setVisibility(inGame && mode == Mode.Everything);
            fogDensityDisplay.setVisibility(inGame && mode == Mode.Everything);
            fogGradientDisplay.setVisibility(inGame && mode == Mode.Everything);
            fogTypeDisplay.setVisibility(inGame && mode == Mode.Everything);
            javaVersionDisplay.setVisibility(mode == Mode.Everything);
            openglVersionDisplay.setVisibility(mode == Mode.Everything);
            gpuDisplay.setVisibility(mode == Mode.Everything);
            windowDimensionsDisplay.setVisibility(mode == Mode.Everything);
            usedMemoryDisplayLeft.setVisibility(mode == Mode.Everything);
            usedMemoryDisplayRight.setVisibility(mode == Mode.Everything);
            allocatedMemoryDisplay.setVisibility(mode == Mode.Everything);
        }

        if (KeyInput.isKeyDown(GLFW_KEY_F5)) {
            mode = Mode.values()[(mode.ordinal() + 1) % Mode.values().length];
        }

        float deltaFogDensity = 0f;
        if (KeyInput.isKeyPressed(GLFW_KEY_LEFT)) deltaFogDensity -= 0.0001f;
        if (KeyInput.isKeyPressed(GLFW_KEY_RIGHT)) deltaFogDensity += 0.0001f;

        float deltaFogGradient = 0f;
        if (KeyInput.isKeyPressed(GLFW_KEY_DOWN)) deltaFogGradient -= 0.1f;
        if (KeyInput.isKeyPressed(GLFW_KEY_UP)) deltaFogGradient += 0.1f;

        if (KeyInput.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
            FogSettings.setUseSphericalFog(!FogSettings.getUseSphericalFog());
        }

        if (deltaFogDensity != 0f) {
            FogSettings.setFogDensity(FogSettings.getFogDensity() + deltaFogDensity);
        }

        if (deltaFogGradient != 0f) {
            FogSettings.setFogGradient(FogSettings.getFogGradient() + deltaFogGradient);
        }

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024 / 1024;

        boolean displayNewValues = false;
        if (Time.getCurrentTime() - lastDisplayTime > REFRESH_TIME) {
            lastDisplayTime = Time.getCurrentTime();
            displayNewValues = true;
        }

        if (lastFps != Main.fps) {
            lastFps = Main.fps;
            fpsDisplay.setText("FPS: " + lastFps);
            fpsDisplay.setPriority(2);
            fpsDisplay.setVisibility(mode == Mode.Everything || mode == Mode.FPS);
        }

        if (lastFrameTime != Time.getDeltaTime() && displayNewValues) {
            lastFrameTime = Time.getDeltaTime();
            frameTimeDisplay.setText(String.format(" - Frame time: %.2f ms", lastFrameTime * 1000f));
            frameTimeDisplay.setPriority(2);
            frameTimeDisplay.setVisibility(mode == Mode.Everything);
        }

        if (lastDrawCalls != MasterRenderer.drawCalls) {
            lastDrawCalls = MasterRenderer.drawCalls;
            drawCallsDisplayRight.setText(String.valueOf(lastDrawCalls));
            drawCallsDisplayRight.setPriority(2);
            drawCallsDisplayRight.setVisibility(mode == Mode.Everything || mode == Mode.DrawCalls);
        }

        if (lastTriangles != MasterRenderer.triangles) {
            lastTriangles = MasterRenderer.triangles;
            trianglesDisplayRight.setText(String.valueOf(lastTriangles));
            trianglesDisplayRight.setPriority(2);
            trianglesDisplayRight.setVisibility(mode == Mode.Everything || mode == Mode.Triangles);
        }

        if (lastAabbCollisions != collisionsAccumulator.collisions) {
            lastAabbCollisions = collisionsAccumulator.collisions;
            aabbCollisionsDisplayRight.setText(String.valueOf(lastAabbCollisions));
            aabbCollisionsDisplayRight.setPriority(2);
            aabbCollisionsDisplayRight.setVisibility(inGame && mode == Mode.Everything);
        }

        ClientLevel clientLevel = ClientLevel.get();
        if (clientLevel != null && !lastPlayerPosition.equals(clientLevel.getPlayer().getPosition())) {
            lastPlayerPosition.set(clientLevel.getPlayer().getPosition());
            playerPositionDisplay.setText(String.format("(x: %.3f, y: %.3f, z: %.3f)", lastPlayerPosition.x, lastPlayerPosition.y, lastPlayerPosition.z));
            playerPositionDisplay.setPriority(2);
            playerPositionDisplay.setVisibility(inGame && mode == Mode.Everything);
        }

        Camera camera = MasterRenderer.currentCamera;
        if (camera != null && (lastCameraOrientation.x != camera.getPitch() || lastCameraOrientation.y != camera.getYaw() || lastCameraOrientation.z != camera.getRoll())) {
            lastCameraOrientation.set(camera.getPitch(), camera.getYaw(), camera.getRoll());
            cameraOrientationDisplay.setText(String.format("pitch: %.0f, yaw: %.0f, roll: %.0f", lastCameraOrientation.x, lastCameraOrientation.y, lastCameraOrientation.z));
            cameraOrientationDisplay.setPriority(2);
            cameraOrientationDisplay.setVisibility(inGame && mode == Mode.Everything);
        }

        if (lastFogType != FogSettings.getUseSphericalFog()) {
            lastFogType = FogSettings.getUseSphericalFog();
            String fogTypeString = lastFogType ? "spherical" : "cylindrical";
            fogTypeDisplay.setText("Fog type: " + fogTypeString);
            fogTypeDisplay.setPriority(2);
            fogTypeDisplay.setVisibility(inGame && mode == Mode.Everything);
        }

        if (lastFogDensity != FogSettings.getFogDensity()) {
            lastFogDensity = FogSettings.getFogDensity();
            fogDensityDisplay.setText("Fog density: " + lastFogDensity);
            fogDensityDisplay.setPriority(2);
            fogDensityDisplay.setVisibility(inGame && mode == Mode.Everything);
        }

        if (lastFogGradient != FogSettings.getFogGradient()) {
            lastFogGradient = FogSettings.getFogGradient();
            fogGradientDisplay.setText("Fog gradient: " + lastFogGradient);
            fogGradientDisplay.setPriority(2);
            fogGradientDisplay.setVisibility(inGame && mode == Mode.Everything);
        }

        if (lastWindowWidth != WindowResizeInput.width || lastWindowHeight != WindowResizeInput.height) {
            lastWindowWidth = WindowResizeInput.width;
            lastWindowHeight = WindowResizeInput.height;
            windowDimensionsDisplay.setText(lastWindowWidth + " x " + lastWindowHeight);
            windowDimensionsDisplay.setPriority(2);
            windowDimensionsDisplay.setVisibility(mode == Mode.Everything);
        }

        if (lastUsedMemory != usedMemory && displayNewValues) {
            lastUsedMemory = usedMemory;
            usedMemoryDisplayRight.setText(String.format("%d MB / %d MB", lastUsedMemory, maxMemory));
            usedMemoryDisplayRight.setPriority(2);
            usedMemoryDisplayRight.setVisibility(mode == Mode.Everything);
        }

        if (lastAllocatedMemory != allocatedMemory) {
            lastAllocatedMemory = allocatedMemory;
            allocatedMemoryDisplay.setText(String.format("Allocated: %d MB", lastAllocatedMemory));
            allocatedMemoryDisplay.setPriority(2);
            allocatedMemoryDisplay.setVisibility(mode == Mode.Everything);
        }

        super.update();
    }

    public static void toggle() {
        if (isOpen) {
            currentInstance.markForDeletion();
            FogSettings.setUseSphericalFog(FogSettings.DEFAULT_USE_SPHERICAL_FOG);
            FogSettings.setFogDensity(FogSettings.DEFAULT_DENSITY);
            FogSettings.setFogGradient(FogSettings.DEFAULT_GRADIENT);
        } else {
            currentInstance = new DebugScreen();
            queueScreenForAddition(currentInstance);
        }

        isOpen = !isOpen;
    }

    private enum Mode {
        Everything, FPS, DrawCalls, Triangles
    }

}
