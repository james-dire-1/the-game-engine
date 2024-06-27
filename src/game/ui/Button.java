package game.ui;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.HoveredComponent;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenSize;

import java.util.function.Consumer;

public class Button extends Gui implements HoveredComponent, ClickedComponent {

    public final String name;

    private Consumer<MouseButton> action;
    public void setAction(Consumer<MouseButton> action) {
        this.action = action;
    }

    public Button(Position position, String name) {
        super(position, new ScreenSize(100, 20), null);
        super.setTextureAndSamplingData("/stall.png");
        super.apply();
        this.name = name;
    }

    @Override
    public void onHovered() {
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (action != null) {
            action.accept(mouseButton);
        }
    }

}
