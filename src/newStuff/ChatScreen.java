package newStuff;

import com.james.input.KeyInput;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.textRendering.TextOrganizer;
import com.james.renderEngine.textRendering.dataTypes.Line;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.NormalizedPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.simulation.ClientLevel;
import game.main.Main;
import org.lwjgl.glfw.GLFW;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatScreen extends Screen {

    private FontInfo font = Main.dustismo;
    private float fontSize = 0.25f;
    private int rows = 10;
    private int screenX = 10;
    private int screenY = 275;
    private int contentWidth = 600;
    private int senderWidth = 200;
    private int additionalLineSpacing = 0;

    private final List<PersistentGuiText> contentEntries = new ArrayList<>();
    private final Map<Integer, PersistentGuiText> senderEntriesMap = new HashMap<>();

    private int entryContentIndex;

    private boolean needsToBeUpdated = true;

    public ChatScreen() {
        Gui gui = new Gui(new NormalizedPosition(1, 1), new ScreenSize(100, 100), null);
        gui.setSingleColor(0, 1, 0);
        gui.apply();
        super.addGui(gui);

        Gui chatBackground = new Gui(new ScreenPosition((contentWidth+senderWidth+screenX)/2, screenY/2), new ScreenSize(contentWidth+senderWidth+screenX, screenY), null);
        chatBackground.setSingleColor(0, 0, 0);
        chatBackground.apply();
        super.addGui(chatBackground);
    }

    public void appendChat(String sender, String fullText) {
        byte[] asciiCodes = fullText.getBytes(StandardCharsets.UTF_8);
        List<Line> lines = TextOrganizer.organizeMultiLineText(font, fontSize, contentWidth, asciiCodes);

        for (int i = 0; i < lines.size(); i++) {
            ScreenPosition entryContentPosition = new ScreenPosition(screenX + senderWidth, 0);

            PersistentGuiText contentEntry = new PersistentGuiText(Collections.singletonList(lines.get(i)), font, fontSize, entryContentPosition);
            contentEntry.setSingleColor(1, 1, 1);
            contentEntry.apply();
            contentEntry.getMesh().isVisible = false;
            super.addGui(contentEntry.getMesh());

            contentEntries.add(contentEntry);

            if (i == 0) {
                ScreenPosition entrySenderPosition = new ScreenPosition(screenX, 0);

                PersistentGuiText senderEntry = new PersistentGuiText(sender, font, fontSize, entrySenderPosition);
                senderEntry.setSingleColor(1, 0, 0);
                senderEntry.apply();
                super.addGui(senderEntry.getMesh());

                int senderEntryIndex = contentEntries.size()-1;
                senderEntriesMap.put(senderEntryIndex, senderEntry);
            }
        }

        entryContentIndex = contentEntries.size() - rows;
    }

    @Override
    public void update() {
        if (ClientLevel.get() == null) {
            super.markForDeletion();
        }

        if (needsToBeUpdated) {
            needsToBeUpdated = false;

            for (PersistentGuiText contentEntry : contentEntries) {
                contentEntry.getMesh().isVisible = false;
            }
            for (PersistentGuiText senderEntry : senderEntriesMap.values()) {
                senderEntry.getMesh().isVisible = false;
            }

            int visibleEntryEndIndex = Math.min(contentEntries.size() - 1, rows - 1);

            for (int i = 0; i <= visibleEntryEndIndex; i++) {
                int currentContentIndex = entryContentIndex + i;
                int yPosition = screenY - (int) (i * (font.lineHeight * fontSize + additionalLineSpacing));

                Gui currentContentEntry = contentEntries.get(currentContentIndex).getMesh();
                ((ScreenPosition) currentContentEntry.position).y = yPosition;
                currentContentEntry.isVisible = true;

                if (senderEntriesMap.containsKey(currentContentIndex)) {
                    Gui currentSenderEntry = senderEntriesMap.get(currentContentIndex).getMesh();
                    ((ScreenPosition) currentSenderEntry.position).y = yPosition;
                    currentSenderEntry.isVisible = true;
                }
            }
        }

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            entryContentIndex++;
            needsToBeUpdated = true;
        }
        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_UP)) {
            entryContentIndex--;
            needsToBeUpdated = true;
        }

        entryContentIndex = Math.max(entryContentIndex, 0);
        entryContentIndex = Math.min(entryContentIndex, contentEntries.size() - rows);

        super.update();
    }

}
