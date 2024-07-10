package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import com.james.simulation.ClientLevel;

public class LoadingScreen extends Screen {

    public LoadingScreen() {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        GuiText text = new GuiText("Loading...", Main.rowdies, 0.3f, GuiText.TextAlignment.CENTER_ALIGNED, new AnchoredPosition(AnchorPoint.CENTER));
        super.addGuis(text.getAllGuis());
    }

    @Override
    public boolean update() {
        if (ClientLevel.get().isReady) {
            super.markForDeletion();
            GLFWUtilities.lockCursor(true);
        }

        return super.update();
    }

}
