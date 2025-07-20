package game.ui.screens;

import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.tools.ColorUtils;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import com.james.simulation.ClientLevel;
import com.james.renderEngine.textRendering.TextAlignment;
import newStuff.ChatScreen;

public class LoadingScreen extends Screen {

    public LoadingScreen() {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        GuiText text = new GuiText("Loading...", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.CENTER));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.apply();
        super.addGuis(text.getAllGuis());
    }

    @Override
    public void update() {
        if (ClientLevel.get().isReady) {
            super.markForDeletion();
            ChatScreen chatScreen = new ChatScreen();
            queueScreenForAddition(chatScreen);
            float[] red = new float[] {1, 0, 0};
            float[] blue = new float[] {0, 0, 1};
            chatScreen.appendChat("pointyfish", red, "Ok so we are testing this right now at the moment, let's see it in action man! I just can't wait!");
            chatScreen.appendChat("pointyfish", red, "public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum public static void main lorem ipsum ");
            chatScreen.appendChat("pointyfish", red, "Oh no! That can't be good. What was that funny noise I just heard.. I think something might be breaking here... :)");
            chatScreen.appendChat("pointyfish", blue, "Ok so we are testing this right now at the moment, let's see it in action man! I just can't wait!");
            chatScreen.appendChat("pointyfish", red, "Oh no! That can't be good. What was that funny noise I just heard.. I think something might be breaking here... :)");
            chatScreen.appendChat("pointyfish", red, "Oh no! That can't be good. What was that funny noise I just heard.. I think something might be breaking here... :)");
            chatScreen.appendChat("pointyfish", red, "Oh no! That can't be good. What was that funny noise I just heard.. I think something might be breaking here... :)");
            chatScreen.appendChat("pointyfish", red, "Oh no! That can't be good. What was that funny noise I just heard.. I think something might be breaking here... :)");
            chatScreen.appendChat("someone else", blue, "hee hee hee hee");

            GLFWUtilities.lockCursor(true);
        }

        super.update();
    }

}
