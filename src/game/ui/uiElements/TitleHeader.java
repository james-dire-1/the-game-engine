package game.ui.uiElements;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import game.main.Main;
import newStuff.TextAlignment;

public class TitleHeader extends GuiText {

    public TitleHeader(String text) {
        super(text, Main.rowdies, 0.5f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -100)));
        super.setAlignment(TextAlignment.CENTER_ALIGNED);
        super.apply();
    }

}
