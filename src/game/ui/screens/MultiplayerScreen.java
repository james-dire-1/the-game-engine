package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import newStuff.ConnectingScreen;
import newStuff.OnlineGameLoader;

public class MultiplayerScreen extends Screen {

    public MultiplayerScreen() {
        SolidBackground background = new SolidBackground(0x5d4073);
        super.addGui(background);

        TitleHeader header = new TitleHeader("Join Online Game");
        super.addGuis(header.getAllGuis());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton goBackButton = new TitleButton("Go Back");
        goBackButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new TitleScreen());
            }
        });
        super.addGuis(goBackButton.getAllGuis());

        TitleButton joinLocalhost = new TitleButton("Connect to localhost");
        joinLocalhost.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new ConnectingScreen("localhost"));
            }
        });
        super.addGuis(joinLocalhost.getAllGuis());

        GuiText text = new GuiText("Proper multiplayer coming soon! :)", Main.rowdies, 0.5f, GuiText.TextAlignment.CENTER_ALIGNED, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -400)));
        super.addGuis(text.getAllGuis());
    }

}
