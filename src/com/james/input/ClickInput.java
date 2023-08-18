package com.james.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class ClickInput extends GLFWMouseButtonCallback {

    private static final boolean[] mousePressed = new boolean[2];
    private static final boolean[] mouseDown = new boolean[2];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        mousePressed[button] = action == GLFW_PRESS;
        mouseDown[button] = action == GLFW_PRESS;
    }

    public static boolean isLeftClickPressed() {
        return mousePressed[0];
    }

    public static boolean isRightClickPressed() {
        return mousePressed[1];
    }

    public static boolean isLeftClickDown() {
        return mouseDown[0];
    }

    public static boolean isRightClickDown() {
        return mouseDown[1];
    }

    public static void resetValues() {
        Arrays.fill(mouseDown, false);
    }

}
