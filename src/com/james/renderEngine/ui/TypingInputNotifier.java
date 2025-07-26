package com.james.renderEngine.ui;

import com.james.input.KeyInput;
import com.james.input.TypingInput;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for notifying Screens whenever typing input has been detected.
 */
public class TypingInputNotifier {

    private static final Map<Screen, TypingInputFunction> functions = new HashMap<>();

    /**
     * Method to be called during game engine initialization to add the onInput() method as a listener for
     * TypingInput and KeyInput. KeyInput is necessary for detecting things like backspace and enter, which aren't
     * detected by TypingInput.
     */
    public static void init() {
        TypingInput.addListener((char character) -> onInput(character, -1));
        KeyInput.addListener((int key) -> onInput('\u0000', key));
    }

    /**
     * Method to be called automatically whenever typing input is detected. Loops through all the Screens that
     * listen for typing input and notifies them through functions.
     */
    public static void onInput(char character, int key) {
        Iterator<Screen> iterator = functions.keySet().iterator();
        while (iterator.hasNext()) {
            Screen screen = iterator.next();

            if (screen.shouldDelete()) {
                iterator.remove();
                continue;
            }

            TypingInputFunction function = functions.get(screen);
            function.invoke(character, key);
        }
    }

    /**
     * Adds a Screen and its respective function to the map of listeners for typing input.
     */
    public static void addScreen(Screen screen, TypingInputFunction function) {
        functions.put(screen, function);
    }

    @FunctionalInterface
    public interface TypingInputFunction {
        void invoke(char character, int key);
    }

}
