package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.uiElements.PersistentGuiText;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import com.james.renderEngine.textRendering.TextAlignment;

import java.io.IOException;

public class FailedToConnectScreen extends Screen {

    public FailedToConnectScreen(String host, IOException e) {
        SolidBackground background = new SolidBackground(0x3f7556);
        super.addGui(background);

        TitleHeader header = new TitleHeader("Failed to connect to \"" + host + "\"");
        super.addGuis(header.getAllGuis());

        PersistentGuiText text = new PersistentGuiText(e.toString(), Main.dustismo, 0.25f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -200)));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.apply();
        super.addGui(text.getMesh());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton okButton = new TitleButton("OK");
        okButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new MultiplayerScreen());
            }
        });
        super.addGuis(okButton.getAllGuis());
    }

}
