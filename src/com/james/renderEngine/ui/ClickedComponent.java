package com.james.renderEngine.ui;

public interface ClickedComponent {
    void onClicked(MouseButton mouseButton);

    enum MouseButton { LEFT, RIGHT }
}
