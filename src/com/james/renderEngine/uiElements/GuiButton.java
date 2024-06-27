package com.james.renderEngine.uiElements;

import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.HoveredComponent;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.Size;

import java.util.function.Consumer;

public class GuiButton extends Gui implements HoveredComponent, ClickedComponent {

    private Consumer<MouseButton> action;
    public void setAction(Consumer<MouseButton> action) {
        this.action = action;
    }

    public GuiButton(Position position, Size size, Gui parent) {
        super(position, size, parent);
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
