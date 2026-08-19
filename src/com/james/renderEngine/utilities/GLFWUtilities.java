package com.james.renderEngine.utilities;

import com.james.input.ClickInput;
import com.james.input.KeyInput;
import com.james.input.MouseMoveInput;
import com.james.input.WindowResizeInput;
import com.james.input.TypingInput;
import com.james.input.ScrollInput;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWUtilities {

    public static boolean shouldClose = false;

    private static long window;

    private static boolean cursorLocked = false;

    private static String currentWindowTitle;

    public static void init(String windowTitle) {
        if (!glfwInit()) throw new RuntimeException("Cannot initialize glfw");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WindowResizeInput.width, WindowResizeInput.height, windowTitle, NULL, NULL);
        if (window == NULL) throw new RuntimeException("Cannot create window");

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        Objects.requireNonNull(vidmode);
        glfwSetWindowPos(window, (vidmode.width() - WindowResizeInput.width) / 2, (vidmode.height() - WindowResizeInput.height) / 2);
        glfwSetKeyCallback(window, new KeyInput());
        glfwSetWindowSizeCallback(window, new WindowResizeInput());
        glfwSetCursorPosCallback(window, new MouseMoveInput());
        glfwSetMouseButtonCallback(window, new ClickInput());
        glfwSetCharCallback(window, new TypingInput());
        glfwSetScrollCallback(window, new ScrollInput());
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        System.out.println("OpenGL version " + glGetString(GL_VERSION));

        currentWindowTitle = windowTitle;
    }

    public static void pollEvents() {
        KeyInput.resetValues();
        MouseMoveInput.resetValues();
        ClickInput.resetValues();
        ScrollInput.resetValues();

        glfwPollEvents();

        if (glfwWindowShouldClose(window) || (
                KeyInput.isKeyDownIgnoreTypingContext(GLFW_KEY_ESCAPE) &&
                KeyInput.isKeyPressedIgnoreTypingContext(GLFW_KEY_D))) {
            shouldClose = true;
        }
    }

    public static void render() {
        glfwSwapBuffers(window);
    }

    public static void lockCursor(boolean shouldCursorLock) {
        int cursorAction = shouldCursorLock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
        glfwSetInputMode(window, GLFW_CURSOR, cursorAction);

        if (!shouldCursorLock && cursorLocked) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);

                glfwGetWindowSize(window, width, height);
                glfwSetCursorPos(window, width.get(0) / 2.0, height.get(0) / 2.0);
            }
        }

        cursorLocked = shouldCursorLock;
    }

    public static boolean isCursorLocked() {
        return cursorLocked;
    }

    public static void setWindowTitle(String windowTitle) {
        if (!currentWindowTitle.equals(windowTitle)) {
            glfwSetWindowTitle(window, windowTitle);
            currentWindowTitle = windowTitle;
        }
    }

    public static void cleanUp() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

}
