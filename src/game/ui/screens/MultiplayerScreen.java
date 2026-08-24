package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.renderEngine.uiElements.tools.CaretBlinker;
import game.main.Main;
import game.ui.uiElements.PersistentTitleHeader;
import game.ui.uiElements.SolidBackground;
import game.ui.uiElements.TitleButton;
import com.james.renderEngine.textRendering.TextAlignment;
import com.james.renderEngine.ui.TypingInputNotifier;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;

public class MultiplayerScreen extends Screen {

    private GuiText field;
    private final CaretBlinker caretBlinker = new CaretBlinker();

    public MultiplayerScreen() {
        TypingInputNotifier.addScreen(this, this::onTypingInput);

        SolidBackground background = new SolidBackground(0x5d4073);
        super.addGui(background);

        PersistentTitleHeader header = new PersistentTitleHeader("Join Online Game");
        super.addGui(header.getMesh());

        TitleButton.resetCurrentVerticalPosition(350);

        TitleButton goBackButton = new TitleButton("Go Back");
        goBackButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new TitleScreen());
            }
        });
        super.addGuis(goBackButton.getAllGuis());

        new TitleButton("");

        TitleButton joinServer = new TitleButton("Connect");
        joinServer.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                queueScreenForAddition(new ConnectingScreen(field.getCurrentText()));
            }
        });
        super.addGuis(joinServer.getAllGuis());

        PersistentGuiText prompt = new PersistentGuiText("Enter server address:", Main.dustismo, 0.3f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(-200, -410)));
        prompt.apply();
        super.addGui(prompt.getMesh());

        this.field = new GuiText("localhost", Main.dustismo, 0.35f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(-200, -445)));
        field.setAlignment(TextAlignment.LEFT_ALIGNED);
        field.makeEditable(this, 30);
        field.apply();
        super.addGuis(field.getAllGuis());
    }

    @Override
    public void update() {
        caretBlinker.update();
        if (caretBlinker.stateChangedThisFrame) field.displayCaret(caretBlinker.blinkState);

        super.update();
    }

    public void onTypingInput(char character, int key) {
        if (character != '\u0000') {
            field.append(character);
        } else if (key == GLFW.GLFW_KEY_BACKSPACE) {
            field.backspace();
        }

        if (character != '\u0000' || key == GLFW_KEY_BACKSPACE) {
            caretBlinker.reset();
        }
    }

}
