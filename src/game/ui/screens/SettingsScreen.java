package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.simulation.ClientLevel;
import game.main.Main;
import game.ui.uiElements.PersistentTitleHeader;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.renderEngine.textRendering.TextAlignment;

public class SettingsScreen extends Screen {

    private final PreviousScreen prevScreen;

    public SettingsScreen(PreviousScreen prevScreen) {
        SolidBackground background = new SolidBackground(0xf27a5c);
        super.addGui(background);

        PersistentTitleHeader header = new PersistentTitleHeader("Options");
        super.addGui(header.getMesh());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton doneButton = new TitleButton("Go Back");
        doneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            super.markForDeletion();

            if (prevScreen == PreviousScreen.TITLE_SCREEN) {
                queueScreenForAddition(new TitleScreen());
            } else if (prevScreen == PreviousScreen.PAUSE_SCREEN) {
                PauseScreen.open();
                PauseScreen.isInSettingsMenu = false;
            }
        });
        super.addGuis(doneButton.getAllGuis());

        PersistentGuiText comingSoon = new PersistentGuiText("More settings options will come in the future :)", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.BOTTOM, new ScreenSize(0, 150)));
        comingSoon.setAlignment(TextAlignment.CENTER_ALIGNED);
        comingSoon.setSingleColor(0, 0, 1);
        comingSoon.wrapAndSetMaxLength(300);
        comingSoon.apply();
        super.addGui(comingSoon.getMesh());

        this.prevScreen = prevScreen;
    }

    // TODO: 2025-07-02 In the future, there should be a better way for deleting ANY screen as a result of the
    // TODO: 2025-07-02 DisconnectedScreen appearing
    @Override
    public void update() {
        if (ClientLevel.get() == null && prevScreen == PreviousScreen.PAUSE_SCREEN) {
            super.markForDeletion();
        }

        super.update();
    }

    public enum PreviousScreen {
        TITLE_SCREEN,
        PAUSE_SCREEN
    }

}
