package game.ui.screens;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import game.main.LocalGameLoader;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;

public class TitleScreen extends Screen {

    public TitleScreen() {
        SolidBackground background = new SolidBackground(0x423227);
        super.addGui(background);

        TitleHeader header = new TitleHeader("Welcome!");
        super.addGuis(header.getAllGuis());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton playButton = new TitleButton("Play");
        playButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen());
                Main.gameLoader = new LocalGameLoader();
            }
        });
        super.addGuis(playButton.getAllGuis());

        TitleButton joinOnlineGameButton = new TitleButton("Join Online Game");
        joinOnlineGameButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new MultiplayerScreen());
            }
        });
        super.addGuis(joinOnlineGameButton.getAllGuis());

        TitleButton settingsButton = new TitleButton("Settings...");
        settingsButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new SettingsScreen(SettingsScreen.PreviousScreen.TITLE_SCREEN));
            }
        });
        super.addGuis(settingsButton.getAllGuis());
    }

}
