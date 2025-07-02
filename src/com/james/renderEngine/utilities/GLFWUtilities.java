package com.james.renderEngine.utilities;

import com.james.input.ClickInput;
import com.james.input.KeyInput;
import com.james.input.MouseMoveInput;
import com.james.input.WindowResizeInput;
import com.james.input.TypingInput;
import newStuff.GLFWWindowTitles;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

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
        assert vidmode != null;
        glfwSetWindowPos(window, (vidmode.width() - WindowResizeInput.width) / 2, (vidmode.height() - WindowResizeInput.height) / 2);
        glfwSetKeyCallback(window, new KeyInput());
        glfwSetWindowSizeCallback(window, new WindowResizeInput());
        glfwSetCursorPosCallback(window, new MouseMoveInput());
        glfwSetMouseButtonCallback(window, new ClickInput());
        glfwSetCharCallback(window, new TypingInput());
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

        glfwPollEvents();

        if (glfwWindowShouldClose(window) || KeyInput.isKeyPressed(GLFW_KEY_ESCAPE)) {
            shouldClose = true;
        }
    }

    public static void render() {
        glfwSwapBuffers(window);
    }

    public static void lockCursor(boolean shouldCursorLock) {
        int cursorAction = shouldCursorLock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
        cursorLocked = shouldCursorLock;
        glfwSetInputMode(window, GLFW_CURSOR, cursorAction);
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
