package com.james.renderEngine.ui.dataTypes;

import com.james.math.Mth;
import org.lwjgl.util.vector.Vector2f;

public class NormalizedSize extends Size {

    public float x;
    public float y;

    public NormalizedSize(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2f normalized() {
        if (gui.parent == null) {
            return new Vector2f(x, y);
        }

        return Mth.multiplyVectors(gui.parent.size.normalized(), new Vector2f(x, y));
    }

    public void setSize(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void changeSize(float x, float y) {
        this.x += x;
        this.y += y;
    }

}
