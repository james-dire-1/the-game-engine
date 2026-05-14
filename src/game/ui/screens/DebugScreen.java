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

    private final GuiText fogDensityDisplay;
    private final GuiText fogGradientDisplay;
    private final GuiText fogTypeDisplay;

    private boolean firstTime = true;

    public DebugScreen() {
        PersistentGuiText howToCloseDisplay = new PersistentGuiText("F3 to close this menu", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, 0)));
        howToCloseDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        howToCloseDisplay.apply();
        super.addGui(howToCloseDisplay.getMesh());

        this.fogTypeDisplay = new GuiText("Fog type: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -25)));
        fogTypeDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogTypeDisplay.makeEditable(this, 0);
        fogTypeDisplay.apply();
        super.addGuis(fogTypeDisplay.getAllGuis());

        this.fogDensityDisplay = new GuiText("Fog density: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -50)));
        fogDensityDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogDensityDisplay.makeEditable(this, 0);
        fogDensityDisplay.apply();
        super.addGuis(fogDensityDisplay.getAllGuis());

        this.fogGradientDisplay = new GuiText("Fog gradient: ", Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(5, -75)));
        fogGradientDisplay.setAlignment(TextAlignment.LEFT_ALIGNED);
        fogGradientDisplay.makeEditable(this, 0);
        fogGradientDisplay.apply();
        super.addGuis(fogGradientDisplay.getAllGuis());

        PersistentGuiText openglVersion = new PersistentGuiText("OpenGL " + GL30.glGetString(GL30.GL_VERSION), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP_RIGHT, new ScreenSize(-5, 0)));
        openglVersion.setAlignment(TextAlignment.RIGHT_ALIGNED);
        openglVersion.apply();
        super.addGui(openglVersion.getMesh());
    }

    @Override
    public void update() {
        if (ClientLevel.get() == null) {
            super.markForDeletion();
            isOpen = false;
        }

        if (firstTime) {
            firstTime = false;
            String fogTypeString = FogSettings.getUseSphericalFog() ? "spherical" : "cylindrical";
            fogTypeDisplay.setText("Fog type: " + fogTypeString);
            fogDensityDisplay.setText("Fog density: " + FogSettings.getFogDensity());
            fogGradientDisplay.setText("Fog gradient: " + FogSettings.getFogGradient());
        }

        if (KeyInput.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
            FogSettings.setUseSphericalFog(!FogSettings.getUseSphericalFog());
            String fogTypeString = FogSettings.getUseSphericalFog() ? "spherical" : "cylindrical";
            fogTypeDisplay.setText("Fog type: " + fogTypeString);
        }

        float deltaFogDensity = 0f;
        if (KeyInput.isKeyPressed(GLFW_KEY_LEFT)) {
            deltaFogDensity -= 0.0001f;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_RIGHT)) {
            deltaFogDensity += 0.0001f;
        }

        float deltaFogGradient = 0f;
        if (KeyInput.isKeyPressed(GLFW_KEY_DOWN)) {
            deltaFogGradient -= 0.1f;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_UP)) {
            deltaFogGradient += 0.1f;
        }

        if (deltaFogDensity != 0f) {
            FogSettings.setFogDensity(FogSettings.getFogDensity() + deltaFogDensity);
            fogDensityDisplay.setText("Fog density: " + FogSettings.getFogDensity());
        }

        if (deltaFogGradient != 0f) {
            FogSettings.setFogGradient(FogSettings.getFogGradient() + deltaFogGradient);
            fogGradientDisplay.setText("Fog gradient: " + FogSettings.getFogGradient());
        }

        super.update();
    }

    public static void toggle() {
        if (isOpen) {
            currentInstance.markForDeletion();
            FogSettings.setUseSphericalFog(true);
            FogSettings.setFogDensity(FogSettings.DEFAULT_DENSITY);
            FogSettings.setFogGradient(FogSettings.DEFAULT_GRADIENT);
        } else {
            currentInstance = new DebugScreen();
            queueScreenForAddition(currentInstance);
        }

        isOpen = !isOpen;
    }

}
