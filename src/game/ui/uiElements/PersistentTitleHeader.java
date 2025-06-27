package game.ui.uiElements;

import com.james.renderEngine.textRendering.coloring.TextColorRules;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.PersistentGuiText;
import game.main.Main;
import com.james.renderEngine.textRendering.TextAlignment;

public class PersistentTitleHeader extends PersistentGuiText {

    public PersistentTitleHeader(String text) {
        this(text, null);
    }

    public PersistentTitleHeader(String text, TextColorRules rules) {
        super(text, Main.rowdies, 0.5f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -100)));
        super.setAlignment(TextAlignment.CENTER_ALIGNED);
        if (rules != null) super.setTextColorRules(rules);
        super.apply();
    }

}
