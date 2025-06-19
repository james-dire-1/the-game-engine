package com.james.renderEngine.ui;

import com.james.tools.RenderingMath;
import com.james.input.MouseMoveInput;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.utilities.VertexUtilityArrays;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UiHandler {

    public static final List<Screen> screens = new ArrayList<>();
    public static final List<Gui> guisToRender = new ArrayList<>();

    public static void update() {
        Iterator<Screen> iterator = screens.iterator();
        while (iterator.hasNext()) {
            Screen screen = iterator.next();

            screen.update();
            if (screen.shouldDelete()) {
                screen.delete();
                iterator.remove();
            }
        }

        List<Screen> screensToBeAdded = Screen.getQueuedScreensAndClear();
        screens.addAll(screensToBeAdded);
    }

    public static boolean isMouseOver(Gui gui) {
        if (gui.vertexPositions != VertexUtilityArrays.defaultVertexPositions)
            throw new RuntimeException("You cannot use clicked components with guis that don't use the default vertex positions!");

        Vector2f normalizedPosition = gui.position.normalized();
        Vector2f normalizedSize = gui.size.normalized();

        int centerX = RenderingMath.asScreenCoordForPosition(normalizedPosition.x, WindowResizeInput.width);
        int centerY = RenderingMath.asScreenCoordForPosition(normalizedPosition.y, WindowResizeInput.height);
        int halfWidth = RenderingMath.asScreenCoordForSize(normalizedSize.x, WindowResizeInput.width) / 2;
        int halfHeight = RenderingMath.asScreenCoordForSize(normalizedSize.y, WindowResizeInput.height) / 2;

        int mouseX = (int) MouseMoveInput.getXPos();
        int mouseY = WindowResizeInput.height - (int) MouseMoveInput.getYPos();

        return mouseX >= centerX - halfWidth && mouseX <= centerX + halfWidth && mouseY >= centerY - halfHeight && mouseY <= centerY + halfHeight;
    }

}
