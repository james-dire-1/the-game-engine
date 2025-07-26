package newStuff;

import com.james.renderEngine.textRendering.TextAlignment;
import com.james.renderEngine.ui.*;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.NormalizedSize;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.tools.ColorUtils;
import game.main.Main;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import org.lwjgl.glfw.GLFW;
import templates.communication.OnlineClientPacketSendEvents;

public class UsernamePromptScreen extends Screen {

    private final GuiText field;

    public UsernamePromptScreen() {
        TypingInputNotifier.addScreen(this, this::onTypingInput);

        Gui background = new Gui(new AnchoredPosition(AnchorPoint.CENTER), new NormalizedSize(1.9f, 1.9f), null);
        float[] blue = ColorUtils.asNormalizedRGBArray(0x63b3e6);
        background.setSingleColor(blue[0], blue[1], blue[2]);
        background.apply();
        super.addGui(background);

        TitleHeader header = new TitleHeader("Please enter a username");
        super.addGuis(header.getAllGuis());

        GuiText prompt = new GuiText("Username:", Main.dustismo, 0.3f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(-200, -200)));
        prompt.apply();
        super.addGuis(prompt.getAllGuis());

        this.field = new GuiText("player", Main.dustismo, 0.35f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(-200, -250)));
        field.setAlignment(TextAlignment.LEFT_ALIGNED);
        field.makeEditable(this, 30);
        field.apply();
        super.addGuis(field.getAllGuis());

        TitleButton.resetCurrentVerticalPosition(350);

        TitleButton submit = new TitleButton("Submit");
        submit.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                // TODO: 2025-07-23 Make this more general perhaps for local games too
                super.markForDeletion();
                new OnlineClientPacketSendEvents().sendPlayerUsername(field.text);
                ChatScreen.localUsername = field.text;
            }
        });
        super.addGuis(submit.getAllGuis());
    }

    public void onTypingInput(char character, int key) {
        if (character != '\u0000') {
            field.append(character);
        } else if (key == GLFW.GLFW_KEY_BACKSPACE) {
            field.backspace();
        }
    }

}
