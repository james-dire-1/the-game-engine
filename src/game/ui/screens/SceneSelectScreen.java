package game.ui.screens;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.tools.ColorUtils;
import game.ui.uiElements.PersistentTitleHeader;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import templates.serverSide.Scenes;

public class SceneSelectScreen extends Screen {

    public SceneSelectScreen() {
        SolidBackground background = new SolidBackground(0xb0c47a);
        super.addGui(background);

        PersistentTitleHeader header = new PersistentTitleHeader("Select a scene:");
        super.addGui(header.getMesh());

        TitleButton.resetCurrentVerticalPosition(175);
        TitleButton goBackButton = new TitleButton("Go Back");
        goBackButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new TitleScreen());
            }
        });
        super.addGuis(goBackButton.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(350);
        TitleButton nothingSceneButton = new TitleButton("Empty", new float[] { 0, 0, 0 }, new float[] { 0.5f, 0.5f, 0.5f });
        nothingSceneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen(Scenes.nothingScene));
            }
        });
        super.addGuis(nothingSceneButton.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(410);
        TitleButton testSceneButton = new TitleButton("Obstacle Course", ColorUtils.asNormalizedRGBArray(0x2269bf));
        testSceneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen(Scenes.testScene));
            }
        });
        super.addGuis(testSceneButton.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(470);
        TitleButton desertSceneButton = new TitleButton("Desert", ColorUtils.asNormalizedRGBArray(0xf7ac5c), ColorUtils.asNormalizedRGBArray(0xb04e43));
        desertSceneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen(Scenes.desertScene));
            }
        });
        super.addGuis(desertSceneButton.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(530);
        TitleButton beachSceneButton = new TitleButton("Beach", ColorUtils.asNormalizedRGBArray(0xff82e2), ColorUtils.asNormalizedRGBArray(0xfff769));
        beachSceneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen(Scenes.beachScene));
            }
        });
        super.addGuis(beachSceneButton.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(590);
        TitleButton plainsSceneButton = new TitleButton("Plains", ColorUtils.asNormalizedRGBArray(0x39916b));
        plainsSceneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new LoadingScreen(Scenes.plainsScene));
            }
        });
        super.addGuis(plainsSceneButton.getAllGuis());
    }

}
