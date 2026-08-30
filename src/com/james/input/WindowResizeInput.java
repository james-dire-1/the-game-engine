package com.james.input;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.util.ArrayList;
import java.util.List;

public class WindowResizeInput extends GLFWWindowSizeCallback {

//    public static int width = 800;
//    public static int height = 600;

    public static int width = 650;

//    public static int width = 1400;
    public static int height = 750;

    private static final List<Action> listeners = new ArrayList<>();

    @Override
    public void invoke(long window, int width, int height) {
        WindowResizeInput.width = width;
        WindowResizeInput.height = height;

        for (Action listener : listeners) {
            listener.invoke();
        }
    }

    public static void addListener(Action listener) {
        listeners.add(listener);
    }

    public interface Action {
        void invoke();
    }

}
