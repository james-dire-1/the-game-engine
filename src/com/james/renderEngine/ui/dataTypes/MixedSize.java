package com.james.renderEngine.ui.dataTypes;

import com.james.math.Mth;
import org.lwjgl.util.vector.Vector2f;

public class MixedSize extends Size {

    public NormalizedSize size;
    public ScreenSize offset;

    public MixedSize(NormalizedSize size, ScreenSize offset) {
        this.size = size;
        this.offset = offset;
    }

    @Override
    public Vector2f normalized() {
        return Mth.add(size.normalized(), offset.normalized());
    }

    public void setSize(float x, float y) {
        size.setSize(x, y);
    }

    public void setOffset(int x, int y) {
        offset.setSize(x, y);
    }

    public void changeSize(float x, float y) {
        size.changeSize(x, y);
    }

    public void changeOffset(int x, int y) {
        offset.changeSize(x, y);
    }

}
