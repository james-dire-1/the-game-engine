package newStuff;

import com.james.input.KeyInput;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.textRendering.TextOrganizer;
import com.james.renderEngine.textRendering.dataTypes.Line;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.TypingInputNotifier;
import com.james.renderEngine.ui.dataTypes.NormalizedPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.simulation.ClientLevel;
import com.james.tools.Time;
import game.main.Main;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class ChatScreen extends Screen {

    private static final float BLINK_TIME = 0.5f;

    private FontInfo font = Main.dustismo;
    private float fontSize = 0.25f;
    private float fieldFontSize = 0.3f;
    private int rows = 15;
    private int screenX = 10;
    private int screenY = 420;
    private int contentWidth = 600;
    private int senderWidth = 200;
    private float messageShowTime = 7;
    private int additionalLineSpacing = 0;

    private final List<PersistentGuiText> contentEntries = new ArrayList<>();
    private final List<Gui> entryBackgrounds = new ArrayList<>();
    private final Map<Integer, PersistentGuiText> senderEntriesMap = new HashMap<>();
    // private final Map<Gui, Float> instantiationTimeMapBackground = new HashMap<>();
    // private final Map<PersistentGuiText, Float> instantiationTimeMapText = new HashMap<>();
    private final Map<Integer, Float> instantiationTimeMap = new HashMap<>();

    private final GuiText field;

    private int entryContentIndex;
    private boolean needsToBeUpdated = true;
    private boolean isOpen = false;
    private float lastBlinkTime = Time.getCurrentTime();
    private boolean blinkState;
    private boolean newChatMessages = false;
    private boolean justClosedChat = false; // to remove enter key bug

    public ChatScreen() {
        TypingInputNotifier.addScreen(this, this::onTypingInput);

        Gui gui = new Gui(new NormalizedPosition(1, 1), new ScreenSize(100, 100), null);
        gui.setSingleColor(0, 1, 0);
        gui.apply();
        super.addGui(gui);

        this.field = new GuiText("", Main.dustismo, fieldFontSize, new ScreenPosition(screenX, (int) (font.lineHeight * fieldFontSize)));
        field.setSingleColor(1, 1, 1);
        field.makeEditable(this, 1000);
        field.apply();
    }

    public void appendChat(String sender, float[] senderColor, String fullText) {
        byte[] asciiCodes = fullText.getBytes(StandardCharsets.UTF_8);
        List<Line> lines = TextOrganizer.organizeMultiLineText(font, fontSize, contentWidth, asciiCodes);

        for (int i = 0; i < lines.size(); i++) {
            ScreenPosition entryBackgroundPosition = new ScreenPosition((contentWidth+senderWidth+screenX)/2 + 5, 0);
            ScreenSize entryBackgroundSize = new ScreenSize(contentWidth+senderWidth+screenX, font.lineHeight * fontSize + additionalLineSpacing - 1);

            Gui entryBackground = new Gui(entryBackgroundPosition, entryBackgroundSize, null);
            entryBackground.setSingleColor(0, 0, 0);
            entryBackground.apply();
            entryBackground.isVisible = false;
            entryBackground.alpha = 0.25f;
            super.addGui(entryBackground);

            entryBackgrounds.add(entryBackground);
//            instantiationTimeMapBackground.put(entryBackground, Time.getCurrentTime());

            ScreenPosition entryContentPosition = new ScreenPosition(screenX + senderWidth, 0);

            PersistentGuiText contentEntry = new PersistentGuiText(Collections.singletonList(lines.get(i)), font, fontSize, entryContentPosition);
            contentEntry.setSingleColor(1, 1, 1);
            contentEntry.apply();
            contentEntry.getMesh().isVisible = false;
            super.addGui(contentEntry.getMesh());

            contentEntries.add(contentEntry);
//            instantiationTimeMapText.put(contentEntry, Time.getCurrentTime());

            instantiationTimeMap.put(contentEntries.size()-1, Time.getCurrentTime());

            if (i == 0) {
                ScreenPosition entrySenderPosition = new ScreenPosition(screenX, 0);

                PersistentGuiText senderEntry = new PersistentGuiText(sender, font, fontSize, entrySenderPosition);
                senderEntry.setSingleColor(senderColor[0], senderColor[1], senderColor[2]);
                senderEntry.apply();
                super.addGui(senderEntry.getMesh());

                int senderEntryIndex = contentEntries.size()-1;
                senderEntriesMap.put(senderEntryIndex, senderEntry);
//                instantiationTimeMapText.put(senderEntry, Time.getCurrentTime());
            }
        }

        entryContentIndex = Math.max(contentEntries.size() - rows, 0);
        newChatMessages = true;
    }

    private int count;
    @Override
    public void update() {
        if (ClientLevel.get() == null) {
            super.markForDeletion();
        }

        if (isOpen && Time.getCurrentTime() - lastBlinkTime >= BLINK_TIME) {
            lastBlinkTime = Time.getCurrentTime();
            blinkState = !blinkState;

            field.displayCarat(blinkState);
        }

        if (needsToBeUpdated || newChatMessages) {
            needsToBeUpdated = false;

            for (PersistentGuiText contentEntry : contentEntries) {
                contentEntry.getMesh().isVisible = false;
            }
            for (Gui entryBackground : entryBackgrounds) {
                entryBackground.isVisible = false;
            }
            for (PersistentGuiText senderEntry : senderEntriesMap.values()) {
                senderEntry.getMesh().isVisible = false;
            }
            field.setVisibility(false);

//            if (isOpen) {
                int visibleEntryEndIndex = Math.min(contentEntries.size() - 1, rows - 1);

                for (int i = 0; i <= visibleEntryEndIndex; i++) {
                    int currentContentIndex = entryContentIndex + i;

                    if (isOpen || instantiationTimeMap.containsKey(currentContentIndex)) {
                        int yPositionCurrentContent = screenY - i * (int) (font.lineHeight * fontSize + additionalLineSpacing);
                        Gui currentContentEntry = contentEntries.get(currentContentIndex).getMesh();
                        ((ScreenPosition) currentContentEntry.position).y = yPositionCurrentContent;
                        currentContentEntry.isVisible = true;

                        int yPositionCurrentBackground = yPositionCurrentContent - (int) ((font.lineHeight * fontSize + additionalLineSpacing) / 2);
                        Gui currentEntryBackground = entryBackgrounds.get(currentContentIndex);
                        ((ScreenPosition) currentEntryBackground.position).y = yPositionCurrentBackground;
                        currentEntryBackground.isVisible = true;

                        if (senderEntriesMap.containsKey(currentContentIndex)) {
                            Gui currentSenderEntry = senderEntriesMap.get(currentContentIndex).getMesh();
                            ((ScreenPosition) currentSenderEntry.position).y = yPositionCurrentContent;
                            currentSenderEntry.isVisible = true;
                        }
                    }
                }

                if (isOpen) {
                    field.setVisibility(true);
                }
//            }

            if (instantiationTimeMap.isEmpty()) {
                newChatMessages = false;
            }
        }

        if (isOpen) {
            if (KeyInput.isKeyDownIgnoreTypingContext(GLFW_KEY_DOWN) || ScrollInput.getYOffset() < -0.5f) {
                entryContentIndex++;
                needsToBeUpdated = true;
            }
            if (KeyInput.isKeyDownIgnoreTypingContext(GLFW_KEY_UP) || ScrollInput.getYOffset() > 0.5f) {
                entryContentIndex--;
                needsToBeUpdated = true;
            }
        }

        entryContentIndex = Math.max(entryContentIndex, 0);
        entryContentIndex = Math.min(entryContentIndex, Math.max(contentEntries.size() - rows, 0));

        // TODO: 2025-07-03 to remove eventually

        if (KeyInput.isKeyDown(GLFW_KEY_U)) {
            appendChat("tomyleej", new float[] {0, 1, 0}, "what's good my g " + count);
            needsToBeUpdated = true;
            count++;
        }

        if (!isOpen && (KeyInput.isKeyDown(GLFW_KEY_SLASH) ||
                        KeyInput.isKeyDown(GLFW_KEY_T) ||
                        (KeyInput.isKeyDown(GLFW_KEY_ENTER) && !justClosedChat))) {
            openChat();
        }
        if (isOpen && KeyInput.isKeyDownIgnoreTypingContext(GLFW_KEY_ESCAPE)) {
            closeChat();
        }

        Iterator<Map.Entry<Integer, Float>> iterator = instantiationTimeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Float> entry = iterator.next();
            float instantiationTime = entry.getValue();

            if (Time.getCurrentTime() - instantiationTime >= messageShowTime) {
                iterator.remove();
            }
        }

        justClosedChat = false;
        super.update();
    }

    public void onTypingInput(char character, int key) {
        if (isOpen) {
            if (character != '\u0000') {
                field.append(character);
            } else if (key == GLFW_KEY_BACKSPACE) {
                field.backspace();
            } else if (key == GLFW_KEY_ENTER) {
                String trimmedChatMessage = field.getCurrentText().trim();
                if (!trimmedChatMessage.equals("")) {
                    field.setText("");
                    appendChat("Player", new float[]{1, 1, 0}, trimmedChatMessage);
                }

                closeChat();
            }

            if (character != '\u0000' || key == GLFW_KEY_BACKSPACE) {
                lastBlinkTime = Time.getCurrentTime();
                field.displayCarat(true);
            }
        }
    }

    private void openChat() {
        isOpen = true;
        needsToBeUpdated = true;
        KeyInput.isTypingContext = true;
    }

    private void closeChat() {
        isOpen = false;
        needsToBeUpdated = true;
        KeyInput.isTypingContext = false;

        entryContentIndex = Math.max(contentEntries.size() - rows, 0);
        justClosedChat = true;
    }

}
