package com.james.renderEngine.ui;

import com.james.renderEngine.ui.dataTypes.Size;
import org.lwjgl.util.vector.Vector2f;

public interface Anchor {
    Vector2f anchor(Vector2f normalizedScale, Vector2f offset);
    Vector2f value(Size size);
    Vector2f valueWithParent(Size size, Gui parent);
}
