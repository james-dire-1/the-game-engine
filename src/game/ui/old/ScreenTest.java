package game.ui.old;

import com.james.renderEngine.ui.Screen;

public class ScreenTest extends Screen {

    /* public ScreenTest() {
        Button topButton = new Button(new NormalizedPosition(0, 0.3f), "top button");
        topButton.setAction((ClickedComponent.MouseButton mouseButton) -> {
            if (mouseButton == ClickedComponent.MouseButton.LEFT) {
                System.out.println("THIS TOP THING WAS CLICKED OH YEAH!!!");
                super.markForDeletion();
                queueScreenForAddition(new TitleScreenTest());
            }
        });
        super.addGui(topButton);

        Button middleButton = new Button(new NormalizedPosition(0, 0), "middle button");
        middleButton.setAction((ClickedComponent.MouseButton mouseButton) -> {
            if (mouseButton == ClickedComponent.MouseButton.RIGHT) {
                super.markForDeletion();
                queueScreenForAddition(new ScreenTest());
            }
        });
        super.addGui(middleButton);

        Button bottomButton = new Button(new NormalizedPosition(0, -0.3f), "bottom button");
        bottomButton.setAction((ClickedComponent.MouseButton mouseButton) -> {
            System.out.println("THIS BOTTOM THING WAS CLICKED OH YEAH!!!");
            super.markForDeletion();
        });
        super.addGui(bottomButton);
    } */

}
