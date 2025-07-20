package newStuff;

import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollInput extends GLFWScrollCallback {
    
    private static double xOffset;
    private static double yOffset;

    @Override
    public void invoke(long window, double newXOffset, double newYOffset) {
        xOffset = newXOffset;
        yOffset = newYOffset;
    }

    public static double getXOffset() {
        return xOffset;
    }

    public static double getYOffset() {
        return yOffset;
    }

    public static void resetValues() {
        xOffset = 0;
        yOffset = 0;
    }

}
