package com.james.main;

import com.james.input.WindowResizeInput;
import com.james.math.MathFunctions;
import com.james.renderEngine.ui.GuiAnimationData;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TitleScreenTest extends Screen {

    private static final float ANIMATION_TIME_IN_SECONDS = 2.5f;

    public TitleScreenTest() {
        /*Button singleplayerButton = new Button(new ScreenPosition(WindowResizeInput.width/2, 200), "Singleplayer " +
                "Button");
        singleplayerButton.setAction((ClickedComponent.MouseButton mouseButton) -> {
            System.out.println("SINGLEPLAYER BUTTON PRESSED");
        });
        super.addGui(singleplayerButton);
        GuiAnimationData singleplayerAnimationData = new GuiAnimationData(GuiAnimationData.Attribute.PositionX, MathFunctions::cubic, 0, ANIMATION_TIME_IN_SECONDS, 100, 600, false);
        super.addAnimationForGui(singleplayerButton, singleplayerAnimationData);

        Button multiplayerButton = new Button(new ScreenPosition(WindowResizeInput.width/2, 250), "Multiplayer Button");
        singleplayerButton.setAction((ClickedComponent.MouseButton mouseButton) -> {
            System.out.println("MULTIPLAYER BUTTON PRESSED");
            //super.markForDeletion();
            queueScreenForAddition(new ScreenTest());
        });
        super.addGui(multiplayerButton);
        GuiAnimationData multiplayerAnimationData = new GuiAnimationData(GuiAnimationData.Attribute.PositionX, MathFunctions::cubic, 0, ANIMATION_TIME_IN_SECONDS, 100, 600, true);
        super.addAnimationForGui(multiplayerButton, multiplayerAnimationData);*/

        List<Function<Float, Float>> array = new ArrayList<>();
        array.add(MathFunctions::linear);
        array.add(MathFunctions::quadratic);
        array.add(MathFunctions::cubic);
        array.add(MathFunctions::sqrt);
        array.add(MathFunctions::sine);

        for (int i = 0; i < 5; i++) {
            Button button = new Button(new ScreenPosition(WindowResizeInput.width/2, i * 50 + 50), String.valueOf(i));
            super.addGui(button);
            GuiAnimationData animationData = new GuiAnimationData(GuiAnimationData.Attribute.PositionX,
                    array.get(i), 0, ANIMATION_TIME_IN_SECONDS, 100, 600, true);
            super.addAnimationForGui(button, animationData);
        }
    }

}
