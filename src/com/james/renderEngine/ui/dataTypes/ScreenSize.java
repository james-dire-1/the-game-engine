package com.james.renderEngine.ui.dataTypes;

import com.james.tools.RenderingMath;
import org.lwjgl.util.vector.Vector2f;

public class ScreenSize extends Size {

    public float x;
    public float y;

    public final float originalX;
    public final float originalY;

    public ScreenSize(float x, float y) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
    }

    @Override
    public Vector2f normalized() {
        return RenderingMath.toNormalizedSize(x, y);
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
