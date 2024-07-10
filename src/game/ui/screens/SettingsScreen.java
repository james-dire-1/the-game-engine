package game.ui.screens;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;

public class SettingsScreen extends Screen {

    private final PreviousScreen prevScreen;

    public SettingsScreen(PreviousScreen prevScreen) {
        this.prevScreen = prevScreen;

        SolidBackground background = new SolidBackground(0xf27a5c);
        super.addGui(background);

        TitleHeader header = new TitleHeader("Options");
        super.addGuis(header.getAllGuis());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton doneButton = new TitleButton("Done");
        doneButton.button.setAction((ClickedComponent.MouseButton button) -> {
            super.markForDeletion();

            if (prevScreen == PreviousScreen.TITLE_SCREEN) {
                queueScreenForAddition(new TitleScreen());
            } else if (prevScreen == PreviousScreen.PAUSE_SCREEN) {
                PauseScreen.open();
                PauseScreen.isInSettingsMenu = false;
            }
        });
        super.addGuis(doneButton.getAllGuis());
    }

    public enum PreviousScreen {
        TITLE_SCREEN,
        PAUSE_SCREEN
    }

}
