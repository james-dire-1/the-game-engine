package com.james.tools;

import org.lwjgl.glfw.GLFW;

/**
 * Collection of useful methods for time.
 */
public class Time {

    // TODO: 2023-08-06 Remove old legacy code for getting system time. glfw's way of getting the time works better for some reason idk

    private static float lastTime;
    private static float deltaTime;

    /**
     * Updates the delta time. This should be called once in each cycle of the game loop.
     * @implNote This is done by assigning deltaTime to the difference between the new current time and the
     * last time this method was called. In addition, this method sets lastTime to the new current time to
     * prepare for the next time this method will be called.
     */
    public static void updateDeltaTime() {
//        deltaTime = lastTime == 0 ? 0 : System.nanoTime() / 1_000_000_000f - lastTime;
//        lastTime = System.nanoTime() / 1_000_000_000f;
        deltaTime = (float) (lastTime == 0 ? 0 : GLFW.glfwGetTime() - lastTime);
        lastTime = (float) GLFW.glfwGetTime();
    }

    /**
     * Returns the amount of time in seconds that has passed between the last two calls of updateDeltaTime()
     */
    public static float getDeltaTime() {
        return deltaTime;
    }

    /**
     * Gets the current time in seconds.
     */
    public static float getCurrentTime() {
//        return System.nanoTime() / 1_000_000_000f;
        return (float) GLFW.glfwGetTime();
    }

}
