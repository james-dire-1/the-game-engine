package com.james.renderEngine.ui.dataTypes;

import com.james.renderEngine.ui.Gui;
import org.lwjgl.util.vector.Vector2f;

public abstract class Size {

    protected Gui gui;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public abstract Vector2f normalized();

}
