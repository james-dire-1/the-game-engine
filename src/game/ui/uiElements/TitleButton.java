package game.ui.uiElements;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.GuiGroup;
import com.james.renderEngine.ui.dataTypes.*;
import com.james.renderEngine.uiElements.GuiButton;
import com.james.renderEngine.uiElements.GuiText;
import com.james.tools.ColorUtils;
import game.main.Main;
import com.james.renderEngine.textRendering.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TitleButton implements GuiGroup {

    private static int currentVerticalPosition;
    private static final int DISTANCE_BETWEEN_BUTTONS = 75;

    public final GuiButton button;
    public final GuiText guiText;
    private final List<Gui> guis = new ArrayList<>();

    public TitleButton(String text) {
        this.button = new GuiButton(new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -currentVerticalPosition)), new ScreenSize(400, 50), null);
        button.setColors(ColorUtils.asNormalizedRGBArray(0x42f5bc, 0x42f58d, 0x42eff5, 0x42f5bc));
        button.apply();
        guis.add(button);

        this.guiText = new GuiText(text.toUpperCase(Locale.ROOT), Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -currentVerticalPosition-10)));
        guiText.setAlignment(TextAlignment.CENTER_ALIGNED);
        guiText.apply();
        guis.addAll(guiText.getAllGuis());

        currentVerticalPosition += DISTANCE_BETWEEN_BUTTONS;
    }

    public static void resetCurrentVerticalPosition() {
        currentVerticalPosition = 250;
    }

    @Override
    public List<Gui> getAllGuis() {
        return guis;
    }
}
