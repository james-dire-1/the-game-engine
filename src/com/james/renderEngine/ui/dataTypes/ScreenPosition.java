package com.james.renderEngine.ui.dataTypes;

import com.james.tools.RenderingMath;
import org.lwjgl.util.vector.Vector2f;

public class ScreenPosition extends Position {

    public int x;
    public int y;

    public ScreenPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2f normalized() {
        if (gui.parent == null) {
            return RenderingMath.toNormalizedPosition(x, y);
        }

        return RenderingMath.add(gui.parent.position.normalized(), RenderingMath.toNormalizedSize(x, y));
    }

    public Vector2f normalizedMixed() {
        return RenderingMath.toNormalizedSize(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void changePosition(int x, int y) {
        this.x += x;
        this.y += y;
    }

}
