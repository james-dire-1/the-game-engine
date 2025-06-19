package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import com.james.simulation.ClientLevel;
import newStuff.TextAlignment;

public class LoadingScreen extends Screen {

    public LoadingScreen() {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        GuiText text = new GuiText("Loading...", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.CENTER));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.apply();
        super.addGuis(text.getAllGuis());
    }

    @Override
    public void update() {
        if (ClientLevel.get().isReady) {
            super.markForDeletion();
            GLFWUtilities.lockCursor(true);
        }

        super.update();
    }

}
