package game.ui.screens;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.tools.Time;
import game.main.LocalGameLoader;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;

public class TitleScreen extends Screen {

    private final TitleHeader header;
    private final TitleButton joinOnlineGameButton;

    public TitleScreen() {
        SolidBackground background = new SolidBackground(0x423227);
        super.addGui(background);

        header = new TitleHeader("Welcome!");
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

        joinOnlineGameButton = new TitleButton("Join Online Game");
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

    private float hue;
    private float instantiationTime = Time.getCurrentTime();

    @Override
    public void update() {
        hue += 0.01f;
        hue %= 1;

//        float[] color = ColorUtils.HSVtoRGB(hue, 1, 1);
//        header.modifySingleColor(color[0], color[1], color[2]);
//        joinOnlineGameButton.guiText.modifySingleColor(color[0], color[1], color[2]);

        header.modifyScales((int index, char character) -> {
            float timeElapsed = Time.getCurrentTime() - instantiationTime;
            float multiplier = (float) (Math.abs(0.125*Math.sin(2*Math.PI*timeElapsed))+0.875);
            float scale = 1 * multiplier;
            return new float[] {scale, scale};
        });

        header.modifyColors((int index, char character) -> {
            float[] color;
            if (index % 2 == 0) {
                color = new float[] {1, 0, 0};
            } else {
                color = new float[] {0, 1, 0};
            }

            return color;
        });

        super.update();
    }
}
