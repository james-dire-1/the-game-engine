package com.james.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class ClickInput extends GLFWMouseButtonCallback {

    private static final boolean[] mousePressed = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private static final boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (button >= 0) {
            mousePressed[button] = action == GLFW_PRESS;
            mouseDown[button] = action == GLFW_PRESS;
        }
    }

    public static boolean isLeftClickPressed() {
        return mousePressed[0];
    }

    public static boolean isRightClickPressed() {
        return mousePressed[1];
    }

    public static boolean isMouseButtonPressed(int button) {
        return mousePressed[button];
    }

    public static boolean isLeftClickDown() {
        return mouseDown[0];
    }

    public static boolean isRightClickDown() {
        return mouseDown[1];
    }

    public static boolean isMouseButtonDown(int button) {
        return mouseDown[button];
    }

    public static void resetValues() {
        Arrays.fill(mouseDown, false);
    }

}
