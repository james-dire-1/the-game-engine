package newStuff;

import com.james.networking.Client;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.simulation.ClientLevel;
import com.james.tools.ThreadManager;
import game.main.Main;
import game.ui.uiElements.SolidBackground;

import java.io.IOException;

public class ConnectingScreen extends Screen {

    public ConnectingScreen(String host) {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        GuiText text = new GuiText("Connecting to remote server...", Main.rowdies, 0.3f, GuiText.TextAlignment.CENTER_ALIGNED, new AnchoredPosition(AnchorPoint.CENTER));
        super.addGuis(text.getAllGuis());

        connectToServer(host);
    }

    private void connectToServer(String host) {
        new Client(host, 6789) {
            @Override
            public void onSuccessfulConnection() {
                ThreadManager.executeOnMainThread(() -> {
                    Main.gameLoader = new OnlineGameLoader();
                });
            }

            @Override
            public void exceptionInConstruction(IOException e) {
                ThreadManager.executeOnMainThread(() -> {
                    ConnectingScreen.super.markForDeletion();
                    UiHandler.screens.add(new FailedToConnectScreen(host, e));
                });
            }
        };
    }

    @Override
    public boolean update() {
        ClientLevel level = ClientLevel.get();

        System.out.println("level = " + level);
        if (level != null) System.out.println("level.isReady = " + level.isReady);
        System.out.println("---------------");

        if (level != null && level.isReady) {
            super.markForDeletion();
            GLFWUtilities.lockCursor(true);
        }

        return super.update();
    }
}
