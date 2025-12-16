package com.james.renderEngine.uiElements;

import com.james.audio.AudioSourcePool;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.HoveredComponent;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.Size;

import java.util.function.Consumer;

public class GuiButton extends Gui implements HoveredComponent, ClickedComponent {

    private boolean hovered = false;
    private boolean lastHovered = false;

    private Consumer<MouseButton> clickAction;
    public void setClickAction(Consumer<MouseButton> clickAction) {
        this.clickAction = clickAction;
    }

    private Action hoverAction;
    public void setHoverAction(Action hoverAction) {
        this.hoverAction = hoverAction;
    }

    private Consumer<Boolean> hoverStateChangeAction;
    public void setHoverStateChangeAction(Consumer<Boolean> hoverStateChangeAction) {
        this.hoverStateChangeAction = hoverStateChangeAction;
    }

    public GuiButton(Position position, Size size, Gui parent) {
        super(position, size, parent);
    }

    public void resetHoverState() {
        hovered = false;
    }

    public void checkHoverStateChanged() {
        if (hoverStateChangeAction != null) {
            if (hovered && !lastHovered) {
                hoverStateChangeAction.accept(true);
            } else if (!hovered && lastHovered) {
                hoverStateChangeAction.accept(false);
            }
        }

        lastHovered = hovered;
    }

    @Override
    public void onHovered() {
        if (hoverAction != null) {
            hoverAction.invoke();
        }

        hovered = true;
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (clickAction != null) {
            clickAction.accept(mouseButton);
        }

        if (soundId != -1) {
            AudioSourcePool.play(soundId, 0.075f);
        }
    }

    @FunctionalInterface
    public interface Action {
        void invoke();
    }

    // Sounds!
    private int soundId = -1;

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

}
