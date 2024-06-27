package com.james.renderEngine.ui.dataTypes;

import com.james.tools.RenderingMath;
import org.lwjgl.util.vector.Vector2f;

public class ScreenSize extends Size {

    public int x;
    public int y;

    public ScreenSize(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2f normalized() {
        return RenderingMath.toNormalizedSize(x, y);
    }

    public void setSize(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void changeSize(int x, int y) {
        this.x += x;
        this.y += y;
    }

}
