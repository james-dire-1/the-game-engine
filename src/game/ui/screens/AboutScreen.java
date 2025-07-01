package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.PersistentGuiText;
import game.main.Main;
import game.ui.uiElements.PersistentTitleHeader;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;

public class AboutScreen extends Screen {

    private static final String ABOUT_TEXT = "Laser Tag is a game built by James in a custom-built engine. The game was " +
            "made to test the capabilities of the engine. The engine has been in the works since the summer of 2022, and " +
            "is only developed during summer and winter breaks. During the summer of 2025, the game engine had just enough " +
            "features for a simple game to be made in it. Thus, Laser Tag was created, and is the first game to be made in " +
            "the game engine.\n\n" +
            "Technical details:\n\n" +
            "The engine and game are programmed in Java. The IntelliJ IDE was used. Models are made in Blender. Textures are " +
            "made in Krita. Code is uploaded on GitHub. If you would like to see it, let James know. \n\n";

    public AboutScreen() {
        SolidBackground background = new SolidBackground(0xdb5151);
        super.addGui(background);

        PersistentTitleHeader header = new PersistentTitleHeader("About:");
        super.addGui(header.getMesh());

        PersistentGuiText text = new PersistentGuiText(ABOUT_TEXT, Main.dustismo, 0.3f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(-300, -250)));
        text.wrapAndSetMaxLength(600);
        text.apply();
        super.addGui(text.getMesh());

        TitleButton.resetCurrentVerticalPosition(175);

        TitleButton doneButton = new TitleButton("Go Back");
        doneButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new TitleScreen());
            }
        });
        super.addGuis(doneButton.getAllGuis());
    }

}
