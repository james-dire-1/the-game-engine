package game.ui.screens;

import com.james.input.KeyInput;
import com.james.renderEngine.textRendering.TextAlignment;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.simulation.ClientLevel;
import game.main.Main;
import com.james.renderEngine.visuals.FogSettings;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;

public class DebugScreen extends Screen {

    private static boolean isOpen = false;
    private static DebugScreen currentInstance;

    private final PersistentGuiText howToCloseDisplay;
    private final PersistentGuiText cannotCloseDisplay;
    private final GuiText fogDensityDisplay;
    private final GuiText fogGradientDisplay;
    private final GuiText fogTypeDisplay;
    private final GuiText usedMemoryDisplayRight;
    private final GuiText allocatedMemoryDisplay;

    private boolean lastFogType;
    private float lastFogDensity;
    private float lastFogGradient;
    private long lastUsedMemory;
    private long lastAllocatedMemory;

    private boolean lastInGame;

    public DebugScreen() {
        PersistentGuiText gameNameDisplay = new PersistentGuiText("Survival Game", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, 0)));
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

        this.fogTypeDisplay = new GuiText("Fog type: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -75)));
        fogTypeDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogTypeDisplay.makeEditable(this, 0);
        fogTypeDisplay.apply();
        fogTypeDisplay.setPriority(2);
        super.addGuis(fogTypeDisplay.getAllGuis());

        this.fogDensityDisplay = new GuiText("Fog density: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -100)));
        fogDensityDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogDensityDisplay.makeEditable(this, 0);
        fogDensityDisplay.apply();
        fogDensityDisplay.setPriority(2);
        super.addGuis(fogDensityDisplay.getAllGuis());

        this.fogGradientDisplay = new GuiText("Fog gradient: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -125)));
        fogGradientDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogGradientDisplay.makeEditable(this, 0);
        fogGradientDisplay.apply();
        fogGradientDisplay.setPriority(2);
        super.addGuis(fogGradientDisplay.getAllGuis());

        PersistentGuiText javaVersion = new PersistentGuiText("Java " + System.getProperty("java.version"), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, 0)));
        javaVersion.setAlignment(TextAlignment.RIGHT_ALIGNED);
        javaVersion.apply();
        javaVersion.setPriority(2);
        super.addGui(javaVersion.getMesh());

        PersistentGuiText openglVersion = new PersistentGuiText("OpenGL " + GL30.glGetString(GL30.GL_VERSION), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -25)));
        openglVersion.setAlignment(TextAlignment.RIGHT_ALIGNED);
        openglVersion.apply();
        openglVersion.setPriority(2);
        super.addGui(openglVersion.getMesh());

        PersistentGuiText usedMemoryDisplayLeft = new PersistentGuiText("Memory used: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-180, -75)));
        usedMemoryDisplayLeft.setAlignment(TextAlignment.RIGHT_ALIGNED);
        usedMemoryDisplayLeft.apply();
        usedMemoryDisplayLeft.setPriority(2);
        super.addGui(usedMemoryDisplayLeft.getMesh());

        this.usedMemoryDisplayRight = new GuiText("", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -75)));
        usedMemoryDisplayRight.setAlignment(TextAlignment.RIGHT_ALIGNED);
        usedMemoryDisplayRight.makeEditable(this, 0);
        usedMemoryDisplayRight.apply();
        usedMemoryDisplayRight.setPriority(2);
        super.addGuis(usedMemoryDisplayRight.getAllGuis());

        this.allocatedMemoryDisplay = new GuiText("Allocated: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, -100)));
        allocatedMemoryDisplay.setAlignment(TextAlignment.RIGHT_ALIGNED);
        allocatedMemoryDisplay.makeEditable(this, 0);
        allocatedMemoryDisplay.apply();
        allocatedMemoryDisplay.setPriority(2);
        super.addGuis(allocatedMemoryDisplay.getAllGuis());
    }

    @Override
    public void update() {
        boolean inGame = ClientLevel.get() != null;
        if (lastInGame != inGame) {
            lastInGame = inGame;

            howToCloseDisplay.setVisibility(inGame);
            cannotCloseDisplay.setVisibility(!inGame);
            fogTypeDisplay.setVisibility(inGame);
            fogDensityDisplay.setVisibility(inGame);
            fogGradientDisplay.setVisibility(inGame);
        }

        float deltaFogDensity = 0f;
        if (KeyInput.isKeyPressed(GLFW_KEY_LEFT))deltaFogDensity -= 0.0001f;
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

        if (lastFogType != FogSettings.getUseSphericalFog()) {
            lastFogType = FogSettings.getUseSphericalFog();
            String fogTypeString = FogSettings.getUseSphericalFog() ? "spherical" : "cylindrical";
            fogTypeDisplay.setText("Fog type: " + fogTypeString);
            fogTypeDisplay.setPriority(2);
        }

        if (lastFogDensity != FogSettings.getFogDensity()) {
            lastFogDensity = FogSettings.getFogDensity();
            fogDensityDisplay.setText("Fog density: " + FogSettings.getFogDensity());
            fogDensityDisplay.setPriority(2);
        }

        if (lastFogGradient != FogSettings.getFogGradient()) {
            lastFogGradient = FogSettings.getFogGradient();
            fogGradientDisplay.setText("Fog gradient: " + FogSettings.getFogGradient());
            fogGradientDisplay.setPriority(2);
        }

        if (lastUsedMemory != usedMemory) {
            lastUsedMemory = usedMemory;
            usedMemoryDisplayRight.setText(String.format("%d MB / %d MB", usedMemory, maxMemory));
            usedMemoryDisplayRight.setPriority(2);
        }

        if (lastAllocatedMemory != allocatedMemory) {
            lastAllocatedMemory = allocatedMemory;
            allocatedMemoryDisplay.setText(String.format("Allocated: %d MB", allocatedMemory));
            allocatedMemoryDisplay.setPriority(2);
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

}
