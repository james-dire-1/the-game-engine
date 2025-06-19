package newStuff;

import org.lwjgl.glfw.GLFWCharCallback;

import java.util.ArrayList;
import java.util.List;

public class TypingInput extends GLFWCharCallback {

    private static final List<Action> listeners = new ArrayList<>();

    @Override
    public void invoke(long window, int codepoint) {
        for (Action listener : listeners) {
            if (codepoint < 0 || codepoint > 127) {
                codepoint = 127;
            }

            listener.invoke((char)codepoint);
        }
    }

    public static void addListener(Action listener) {
        listeners.add(listener);
    }

    public interface Action {
        void invoke(char character);
    }

}
