package newStuff;

import com.james.input.KeyInput;
import com.james.renderEngine.ui.Screen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TypingInputNotifier {

    private static final Map<Screen, TypingInputFunction> functions = new HashMap<>();

    public static void init() {
        TypingInput.addListener((char character) -> onInput(character, -1));
        KeyInput.addListener((int key) -> onInput('\u0000', key));
    }

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

    public static void addScreen(Screen screen, TypingInputFunction function) {
        functions.put(screen, function);
    }

    @FunctionalInterface
    public interface TypingInputFunction {
        void invoke(char character, int key);
    }

}
