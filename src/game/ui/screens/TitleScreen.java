package game.ui.screens;

import com.james.renderEngine.textRendering.TextAlignment;
import com.james.renderEngine.textRendering.TextOrganizer;
import com.james.renderEngine.textRendering.coloring.CharacterColor;
import com.james.renderEngine.textRendering.coloring.ColorContents;
import com.james.renderEngine.textRendering.coloring.TextColorRules;
import com.james.renderEngine.textRendering.dataTypes.Line;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.tools.ColorUtils;
import com.james.tools.Time;
import game.ui.uiElements.PersistentTitleHeader;
import templates.settings.GLFWWindowTitles;
import templates.gameplay.LocalGameLoader;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class TitleScreen extends Screen {

    private static final String WELCOME_TEXT = "Welcome to Laser Tag!";

    private final PersistentTitleHeader header;
    private final float instantiationTime = Time.getCurrentTime();

    public TitleScreen() {
        GLFWUtilities.setWindowTitle(GLFWWindowTitles.MAIN);

        SolidBackground background = new SolidBackground(0x423227);
        super.addGui(background);

        int middleIndex = WELCOME_TEXT.length() / 2;
        TextColorRules rules = new TextColorRules((Integer index, Character character) -> {
            float progress = (index % (middleIndex)) / (float) middleIndex;

            if (index < middleIndex) {
                return new ColorContents(0, 1, progress);
            } else {
                return new ColorContents(1, 2, progress);
            }
        });
        float[] blue = ColorUtils.asNormalizedRGBArray(0x2eeeff);
        float[] yellow = ColorUtils.asNormalizedRGBArray(0xe7fa1b);
        float[] pink = ColorUtils.asNormalizedRGBArray(0xfa2ddb);
        float[] lime = ColorUtils.asNormalizedRGBArray(0x50ed2d);
        rules.addCharacterColor(CharacterColor.setSingleColor(blue[0], blue[1], blue[2]));
        rules.addCharacterColor(CharacterColor.setSingleColor(yellow[0], yellow[1], yellow[2]));
        rules.addCharacterColor(CharacterColor.setSingleColor(pink[0], pink[1], pink[2]));

        header = new PersistentTitleHeader(WELCOME_TEXT, rules);
        super.addGui(header.getMesh());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton joinOnlineGameButton = new TitleButton("Join Online Game", blue);
        joinOnlineGameButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new MultiplayerScreen());
            }
        });
        super.addGuis(joinOnlineGameButton.getAllGuis());

        TitleButton settingsButton = new TitleButton("Settings...", yellow);
        settingsButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new SettingsScreen(SettingsScreen.PreviousScreen.TITLE_SCREEN));
            }
        });
        super.addGuis(settingsButton.getAllGuis());

        TitleButton aboutButton = new TitleButton("About", pink);
        aboutButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new AboutScreen());
            }
        });
        super.addGuis(aboutButton.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(700);

        TitleButton playButton = new TitleButton("Debug Mode", lime);
        playButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen());
                Main.gameLoader = new LocalGameLoader();
            }
        });
        super.addGuis(playButton.getAllGuis());

        PersistentGuiText versionNumber = new PersistentGuiText("In Development!", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.BOTTOM_RIGHT, new ScreenSize(-15, 40)));
        versionNumber.setAlignment(TextAlignment.RIGHT_ALIGNED);
        float[] limeGreen = ColorUtils.asNormalizedRGBArray(0xcafc14);
        versionNumber.setSingleColor(limeGreen[0], limeGreen[1], limeGreen[2]);
        versionNumber.apply();
        super.addGui(versionNumber.getMesh());

        String message = "Lorem ipsum lorem ipsum dfasdf asdfasdf dfsdf dfd dsf etht fhsa hh rdg sdfgsdg fhfhfghfh Lorem ipsum lorem ipsum dfasdf asdfasdf dfsdf dfd dsf etht fhsa hh rdg sdfgsdg fhfhfghfh Lorem ipsum lorem ipsum dfasdf asdfasdf dfsdf dfd dsf etht fhsa hh rdg sdfgsdg fhfhfghfh Lorem ipsum lorem ipsum dfasdf asdfasdf dfsdf dfd dsf etht fhsa hh rdg sdfgsdg fhfhfghfh ";
        List<Line> lines = TextOrganizer.organizeMultiLineText(Main.dustismo, 0.3f, 300, message.getBytes(StandardCharsets.US_ASCII));

        int yPosition = 30;
        for (Line line : lines) {
            yPosition += -60;

            PersistentGuiText testing = new PersistentGuiText(Collections.singletonList(line), Main.dustismo, 0.3f, new AnchoredPosition(AnchorPoint.TOP_LEFT, new ScreenSize(30, yPosition)));
            testing.wrapAndSetMaxLength(300);
            testing.setSingleColor(1, 0, 0);
            testing.apply();
            super.addGui(testing.getMesh());
        }
    }

    @Override
    public void update() {
        float timeElapsed = Time.getCurrentTime() - instantiationTime;
        float multiplier = (float) (0.05*Math.sin(0.25*Math.PI*timeElapsed)+1.2);
        float scale = 1 * multiplier;

        header.modifyGlobalScale(scale, scale);

        super.update();
    }
}
