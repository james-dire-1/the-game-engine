package game.ui.screens;

import com.james.renderEngine.textRendering.TextAlignment;
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

public class DisconnectedScreen extends Screen {

    public DisconnectedScreen(Exception e) {
        SolidBackground background = new SolidBackground(0x333333);
        super.addGui(background);

        PersistentTitleHeader header = new PersistentTitleHeader("You got disconnected from the server:");
        header.modifyGlobalColor(1, 1, 1);
        super.addGui(header.getMesh());

        PersistentGuiText text = new PersistentGuiText(e.getMessage(), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -200)));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.setSingleColor(1, 1, 1);
        text.apply();
        super.addGui(text.getMesh());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton okButton = new TitleButton("OK");
        okButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new TitleScreen());
            }
        });
        super.addGuis(okButton.getAllGuis());
    }

}
