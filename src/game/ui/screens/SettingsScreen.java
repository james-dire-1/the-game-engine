package game.ui.screens;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.NormalizedPosition;
import com.james.tools.Time;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.renderEngine.textRendering.TextAlignment;

public class SettingsScreen extends Screen {

//    private final GuiText comingSoon;
//    private final PersistentGuiText comingSoon2;

    private final PreviousScreen prevScreen;

    private final float instantiationTime;

    public SettingsScreen(PreviousScreen prevScreen) {
        this.instantiationTime = Time.getCurrentTime();

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

//        this.comingSoon = new GuiText("More settings options will come in the future :)", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.BOTTOM, new ScreenSize(0, 75)));
//        comingSoon.setAlignment(TextAlignment.CENTER_ALIGNED);
//        comingSoon.setSingleColor(0, 0, 1);
//        comingSoon.wrapAndSetMaxLength(300);
//        comingSoon.apply();
//        super.addGuis(comingSoon.getAllGuis());
//
//        this.comingSoon2 = new PersistentGuiText("More settings options will come in the future :)", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.BOTTOM, new ScreenSize(0, 150)));
//        comingSoon2.setAlignment(TextAlignment.CENTER_ALIGNED);
//        comingSoon2.setSingleColor(0, 0, 1);
//        comingSoon2.wrapAndSetMaxLength(300);
//        comingSoon2.apply();
//        super.addGui(comingSoon2.getMesh());

        PersistentGuiText test = new PersistentGuiText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis maximus varius augue, non ornare sem venenatis quis. Curabitur vitae facilisis ligula. Aliquam cursus, dolor vitae hendrerit accumsan, elit turpis tincidunt tortor, a tincidunt lorem diam vel sapien. Proin vel enim feugiat, congue eros ac, dignissim risus. Nam dui neque, sollicitudin nec ipsum non, mattis mattis velit. Cras sit amet convallis tellus, quis varius odio. Integer ultricies dapibus ultrices. Donec tristique rhoncus ipsum ut lobortis. Pellentesque in elementum nulla, elementum sodales libero.\n" +
                "\n" +
                "Sed congue arcu fringilla nisi blandit ornare at in purus. Ut mattis ac lacus sit amet tempus. Suspendisse quis ante ornare, bibendum justo eu, rutrum metus. Duis scelerisque erat sit amet risus pretium, at pulvinar neque venenatis. Praesent cursus nisi sit amet bibendum lobortis. Sed dapibus varius libero, eget egestas libero convallis quis. Nunc porta mauris at purus malesuada, in elementum libero scelerisque. Cras facilisis, magna ac convallis tincidunt, magna augue imperdiet odio, ut aliquam nunc dolor quis eros. Fusce aliquam mollis arcu quis accumsan. Sed non sem vitae ipsum lobortis varius. Vivamus elementum lacinia ex, vel ultrices quam facilisis ut. In et erat egestas, auctor leo sit amet, rhoncus felis. Quisque sit amet vehicula enim. Nam ultricies vestibulum ligula, quis aliquet orci consectetur id.", Main.rowdies, 0.3f, new NormalizedPosition(0, 1));
        test.setAlignment(TextAlignment.CENTER_ALIGNED);
        test.setSingleColor(0, 0, 1);
        test.wrapAndSetMaxLength(800);
        test.justify();
        test.apply();
        super.addGui(test.getMesh());
    }

    @Override
    public void update() {
        float timeElapsed = Time.getCurrentTime() - instantiationTime;

        float globalSize = (float) (Math.abs(0.125*Math.sin(2*Math.PI*timeElapsed))+0.875);

//        comingSoon2.modifyGlobalScale(globalSize, globalSize);

        super.update();
    }

    public enum PreviousScreen {
        TITLE_SCREEN,
        PAUSE_SCREEN
    }

}
