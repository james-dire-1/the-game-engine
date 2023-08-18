package com.james.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseMoveInput extends GLFWCursorPosCallback {

    private static double xPos;
    private static double yPos;

    private static double deltaX;
    private static double deltaY;

    @Override
    public void invoke(long window, double newXPos, double newYPos) {
        deltaX = newXPos - xPos;
        deltaY = newYPos - yPos;
        xPos = newXPos;
        yPos = newYPos;
    }

    public static double getXPos() {
        return xPos;
    }

    public static double getYPos() {
        return yPos;
    }

    public static double getDeltaX() {
        return deltaX;
    }

    public static double getDeltaY() {
        return deltaY;
    }

    public static void resetValues() {
        deltaX = 0;
        deltaY = 0;
    }

}
