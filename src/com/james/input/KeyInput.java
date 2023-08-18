package com.james.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWNativeWin32;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput extends GLFWKeyCallback {

    private static final boolean[] keysPressed = new boolean[GLFW_KEY_LAST];
    private static final boolean[] keysDown = new boolean[GLFW_KEY_LAST];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key >= 0) {
            keysPressed[key] = action != GLFW_RELEASE;
            keysDown[key] = action != GLFW_RELEASE;
        }
    }

    public static boolean isKeyPressed(int key) {
        return keysPressed[key];
    }

    public static boolean isKeyDown(int key) {
        return keysDown[key];
    }

    public static void resetValues() {
        Arrays.fill(keysDown, false);
    }

}
