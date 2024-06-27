package com.james.renderEngine.ui.dataTypes;

import com.james.tools.RenderingMath;
import org.lwjgl.util.vector.Vector2f;

public class NormalizedPosition extends Position {

    public float x;
    public float y;

    public NormalizedPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2f normalized() {
        if (gui.parent == null) {
            return new Vector2f(x, y);
        }

        return RenderingMath.add(gui.parent.position.normalized(), new Vector2f(x, y));
    }

    public Vector2f simpleNormalized() {
        return new Vector2f(x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void changePosition(float x, float y) {
        this.x += x;
        this.y += y;
    }

}
