package com.james.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput extends GLFWKeyCallback {

    private static final boolean[] keysPressed = new boolean[GLFW_KEY_LAST];
    private static final boolean[] keysDown = new boolean[GLFW_KEY_LAST];

    private static final List<Action> listeners = new ArrayList<>();

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key >= 0) {
            keysPressed[key] = action != GLFW_RELEASE;
            keysDown[key] = action != GLFW_RELEASE;
        }

        if (action != GLFW_RELEASE) {
            for (Action listener : listeners) {
                listener.invoke(key);
            }
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

    public static void addListener(Action listener) {
        listeners.add(listener);
    }

    public interface Action {
        void invoke(int key);
    }

}
