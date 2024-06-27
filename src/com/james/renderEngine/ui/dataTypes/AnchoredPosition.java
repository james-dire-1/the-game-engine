package com.james.renderEngine.ui.dataTypes;

import com.james.tools.RenderingMath;
import com.james.renderEngine.ui.AnchorPoint;
import org.lwjgl.util.vector.Vector2f;

public class AnchoredPosition extends Position {

    private Size size;
    private AnchorPoint anchorPoint;
    private Size offset;

    public AnchoredPosition(AnchorPoint anchorPoint) {
        this.anchorPoint = anchorPoint;
        this.offset = new ScreenSize(0, 0);
    }

    public AnchoredPosition(AnchorPoint anchorPoint, Size offset) {
        this.anchorPoint = anchorPoint;
        this.offset = offset;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public Vector2f normalized() {
        if (gui.parent == null) {
            return RenderingMath.add(anchorPoint.value(size), offset.normalized());
        }

        return RenderingMath.add(anchorPoint.valueWithParent(size, gui.parent), offset.normalized());
    }

}
