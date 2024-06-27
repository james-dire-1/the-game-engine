package game.ui.uiElements;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import game.main.Main;

public class TitleHeader extends GuiText {

    public TitleHeader(String text) {
        super(text, Main.rowdies, 0.5f, TextAlignment.CENTER_ALIGNED, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -100)));
    }

}
