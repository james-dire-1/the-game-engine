package game.ui.uiElements;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.GuiGroup;
import com.james.renderEngine.ui.dataTypes.*;
import com.james.renderEngine.uiElements.GuiButton;
import com.james.renderEngine.uiElements.PersistentGuiText;
import game.main.Main;
import com.james.renderEngine.textRendering.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class TitleButton implements GuiGroup {

    private static final float[] GRAY_WHITE = new float[] {0.85f, 0.85f, 0.85f};

    private static int currentVerticalPosition;
    private static final int DISTANCE_BETWEEN_BUTTONS = 75;

    public final GuiButton button;
    public final PersistentGuiText persistentGuiText;
    private final List<Gui> guis = new ArrayList<>();

    private final float[] normalColor;
    private float[] highlightColor = new float[] {1, 1, 1};

    public TitleButton(String text) {
        this(text, GRAY_WHITE);
    }

    public TitleButton(String text, float[] normalColor) {
        this.button = new GuiButton(new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -currentVerticalPosition)), new ScreenSize(449 * 0.9f, 46 * 0.9f), null);
        button.setTextureAndSamplingData("/textures/title-button.png");
        button.setSingleColor(normalColor[0], normalColor[1], normalColor[2]);
        button.setHoverStateChangeAction(this::setHighlighted);

        button.apply();
        guis.add(button);

        this.persistentGuiText = new PersistentGuiText(text, Main.dustismo, 0.3f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, -currentVerticalPosition-7)));
        persistentGuiText.setAlignment(TextAlignment.CENTER_ALIGNED);
        persistentGuiText.apply();
        guis.add(persistentGuiText.getMesh());

        this.normalColor = normalColor;

        currentVerticalPosition += DISTANCE_BETWEEN_BUTTONS;
    }

    public void setHighlightColor(float[] highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            button.setSingleColor(highlightColor[0], highlightColor[1], highlightColor[2]);
        } else {
            button.setSingleColor(normalColor[0], normalColor[1], normalColor[2]);
        }
    }

    @Override
    public List<Gui> getAllGuis() {
        return guis;
    }

    public static void resetCurrentVerticalPosition() {
        resetCurrentVerticalPosition(250);
    }

    public static void resetCurrentVerticalPosition(int position) {
        currentVerticalPosition = position;
    }

}
