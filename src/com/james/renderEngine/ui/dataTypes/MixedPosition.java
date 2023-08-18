package com.james.renderEngine.ui.dataTypes;

import com.james.renderEngine.ui.Gui;
import org.lwjgl.util.vector.Vector2f;

public class MixedPosition extends Position {

    public NormalizedPosition position;
    public ScreenPosition offset;

    public MixedPosition(NormalizedPosition position, ScreenPosition offset) {
        this.position = position;
        this.offset = offset;
    }

    public MixedPosition(float normalizedX, float normalizedY, int screenX, int screenY) {
        this.position = new NormalizedPosition(normalizedX, normalizedY);
        this.offset = new ScreenPosition(screenX, screenY);
    }

    public void setGuiForMembers(Gui gui) {
        position.setGui(gui);
        offset.setGui(gui);
    }

    @Override
    public Vector2f normalized() {
        if (gui.parent == null) {
            return Vector2f.add(position.simpleNormalized(), offset.normalizedMixed(), null);
        }

        return Vector2f.add(gui.parent.position.normalized(), Vector2f.add(position.simpleNormalized(), offset.normalizedMixed(), null), null);
    }

    public void setPosition(float x, float y) {
        position.setPosition(x, y);
    }

    public void setOffset(int x, int y) {
        offset.setPosition(x, y);
    }

    public void changePosition(float x, float y) {
        position.changePosition(x, y);
    }

    public void changeOffset(int x, int y) {
        offset.changePosition(x, y);
    }

}
