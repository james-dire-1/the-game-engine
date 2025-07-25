package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import com.james.tools.ColorUtils;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import com.james.simulation.ClientLevel;
import com.james.renderEngine.textRendering.TextAlignment;
import newStuff.ChatScreen;
import templates.communication.LocalServerPacketSendEvents;
import templates.communication.LocalServerProperties;

public class LoadingScreen extends Screen {

    public LoadingScreen() {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        GuiText text = new GuiText("Loading...", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.CENTER));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.apply();
        super.addGuis(text.getAllGuis());

        new LevelInitializer("main", new LocalServerPacketSendEvents());
        LevelInitializer.isOnlineGame = false;
        LocalServerProperties.clientJoined();
    }

    @Override
    public void update() {
        ClientLevel level = ClientLevel.get();

        if (level != null && level.isReady) {
            super.markForDeletion();
            queueScreenForAddition(new ChatScreen());

            GLFWUtilities.lockCursor(true);
        }

        super.update();
    }

}
