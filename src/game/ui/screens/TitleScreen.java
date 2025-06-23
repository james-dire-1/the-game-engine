package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.tools.Time;
import game.main.LocalGameLoader;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import newStuff.*;

public class TitleScreen extends Screen {

    private final TitleHeader header;
    private final TitleButton joinOnlineGameButton;

    private final ColorContents c1;
    private final ColorContents c5and6;
    private final GuiText testing4;

    public TitleScreen() {
        SolidBackground background = new SolidBackground(0x423227);
        super.addGui(background);

        GuiText testing = new GuiText("This is for testing", Main.rowdies, 0.35f, new AnchoredPosition(AnchorPoint.LEFT));
        testing.apply();
        testing.modifyColors((int index, char id) -> {
            return new float[] {index / 10f, 1 - (index / 10f), 0};
        });
        super.addGuis(testing.getAllGuis());

        PersistentGuiText testing2 = new PersistentGuiText("This is also for testing", Main.rowdies, 0.35f, new AnchoredPosition(AnchorPoint.RIGHT));
        testing2.setAlignment(TextAlignment.RIGHT_ALIGNED);
        testing2.setSingleColor(1, 1, 0);
        testing2.apply();
        super.addGui(testing2.getMesh());

        ColorContents c0 = new ColorContents(0);
        ColorContents c2 = new ColorContents(2);
        ColorContents c3 = new ColorContents(3);
        ColorContents c4 = new ColorContents(4);
        c1 = new ColorContents(1, 0, 0);
        c5and6 = new ColorContents(5, 6, 0);

        TextColorRules rules = new TextColorRules((Character character, Integer index) -> {
            if (character == 'e' || character == 't') {
                return c0;
            } else if (index % 5 == 0) {
                return c2;
            } else if (character == 'o') {
                return c3;
            } else if (character == 'r') {
                return c4;
            } else if (index == 3 || index == 6 || index == 18) {
                return c5and6;
            } else {
                return c1;
            }
        });
        rules.addCharacterColor(CharacterColor.setSingleColor(1, 0, 1));
        rules.addCharacterColor(CharacterColor.setSingleColor(0, 1, 1));
        rules.addCharacterColor(CharacterColor.setLeftRightColors(new float[] {1, 0, 0}, new float[] {1, 1, 0}));
        rules.addCharacterColor(CharacterColor.setDownUpColors(new float[] {0, 1, 0}, new float[] {1, 1, 1}));
        rules.addCharacterColor(CharacterColor.setSingleColor(1, 1, 0));
        rules.addCharacterColor(CharacterColor.setSingleColor(1, 0, 0));
        rules.addCharacterColor(CharacterColor.setSingleColor(1, 1, 0));

        PersistentGuiText testing3 = new PersistentGuiText("This is for testing fr hoss", Main.rowdies, 0.6f, new AnchoredPosition(AnchorPoint.BOTTOM, new ScreenSize(0, 100)));
        testing3.setAlignment(TextAlignment.CENTER_ALIGNED);
        testing3.setTextColorRules(rules);
        testing3.apply();
        super.addGui(testing3.getMesh());

        testing4 = new GuiText("This is for testing fr hoss", Main.rowdies, 0.6f, new AnchoredPosition(AnchorPoint.BOTTOM, new ScreenSize(0, 200)));
        testing4.setAlignment(TextAlignment.CENTER_ALIGNED);
        testing4.setTextColorRules(rules);
        testing4.apply();
        super.addGuis(testing4.getAllGuis());

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

        float progress = (Time.getCurrentTime() % 1f);

//        float[] color = ColorUtils.HSVtoRGB(hue, 1, 1);
//        header.modifySingleColor(color[0], color[1], color[2]);
//        joinOnlineGameButton.guiText.modifySingleColor(color[0], color[1], color[2]);


        c5and6.progress = progress;

//        testing4.applyUpdatedColorContentsForCharIndex(6, null);
//        testing4.applyUpdatedColorContentsForCharIndex(3, null);
//        testing4.applyUpdatedColorContentsForCharIndex(18, null);


        testing4.applyUpdatedColorContentsForAllChars();

//        testing4.modifyColors((int index, char character) -> {
//            return new float[] {1, progress, 0};
//        });

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
