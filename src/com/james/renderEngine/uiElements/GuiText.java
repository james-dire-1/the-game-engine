package com.james.renderEngine.uiElements;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.GuiGroup;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import newStuff.*;

import java.util.List;

/**
 * Creates a new text gui.
 */
public class GuiText extends AbstractGuiText implements GuiGroup {

    public GuiText(String text, FontInfo font, float fontSize, Position position) {
        super(text, font, fontSize, position);
        this.currentText = new StringBuilder(text);
    }

    @Override
    public void modifyGlobalColor(float r, float g, float b) {
        for (Gui textQuad : master.children) {
            textQuad.setSingleColor(r, g, b);
        }
    }

    @Override
    public void modifyGlobalScale(float globalScaleX, float globalScaleY) {
        for (Gui textQuad : master.children) {
            ScreenSize size = (ScreenSize) textQuad.size;
            size.setSize(globalScaleX * size.originalX, globalScaleY * size.originalY);
        }
    }

    // TODO: 2024-12-29 This is going to have some interesting documentation!
    public void modifyColors(GuiTextFunction function) {
        char[] characters = super.text.replace(" ", "").toCharArray();

        for (int i = 0; i < characters.length; i++) {
            float[] color = function.getProperty(i, characters[i]);
            master.children.get(i).setSingleColor(color[0], color[1], color[2]);
        }
    }

    public void modifyScales(GuiTextFunction function) {
        char[] characters = super.text.replace(" ", "").toCharArray();

        for (int i = 0; i < characters.length; i++) {
            float[] scale = function.getProperty(i, characters[i]);
            ScreenSize size = (ScreenSize) master.children.get(i).size;
            size.setSize(scale[0] * size.originalX, scale[1] * size.originalY);
        }
    }

    public void applyUpdatedColorContentsForAllChars() {
        char[] charactersWithoutSpaces = text.replace(" ", "").toCharArray();

        for (int i = 0; i < charactersWithoutSpaces.length; i++) {
            applyUpdatedColorContentsForCharIndex(i, charactersWithoutSpaces);
        }
    }

    // TODO: 2025-06-23 When writing up the documentation, note the similarities between this method and
    // TODO: 2025-06-23 what is found in the TextMeshCreator
    public void applyUpdatedColorContentsForCharIndex(int index, char[] charactersWithoutSpaces) {
        if (charactersWithoutSpaces == null) {
            charactersWithoutSpaces = text.replace(" ", "").toCharArray();
        }

        float[] colors = textColorRules.getCharacterColor(index, charactersWithoutSpaces[index]);
        Gui guiForCharacter = master.children.get(index);

        if (colors.length == 3) {
            float r = colors[0];
            float g = colors[1];
            float b = colors[2];

            guiForCharacter.setSingleColor(r, g, b);
        }
        // if colors.length == 12, simply do nothing
        // since we can't modify color buffers at the moment

        guiForCharacter.apply();
    }

    private final StringBuilder currentText;
    private Screen screen;
    private int maxCharCount;

    public void makeEditable(Screen screen, int maxCharCount) {
        if (maxCharCount < 0)
            throw new RuntimeException();

        this.screen = screen;
        this.maxCharCount = maxCharCount;
    }

    public void append(char character) {
        if (character == '\n' && maxLength == 0) {
            return;
        }

        if (maxCharCount == 0 || currentText.length() < maxCharCount) {
            screen.removeGuis(master.children);
            currentText.append(character);
            text = currentText.toString();
            apply();
            screen.addGuis(master.children);
        }
    }

    public void backspace() {
        if (currentText.length() > 0) {
            screen.removeGuis(master.children);
            currentText.deleteCharAt(currentText.length() - 1);
            text = currentText.toString();
            apply();
            screen.addGuis(master.children);
        }
    }

    public void setText(String newText) {
        screen.removeGuis(master.children);
        currentText.replace(0, currentText.length(), newText);
        text = currentText.toString();
        apply();
        screen.addGuis(master.children);
    }

    private Gui master;

    @Override
    public void apply() {
        List<Line> lines = super.getLines();
        this.master = TextMeshCreator.createGuis(position, singleColor, textColorRules, text, font, fontSize, lines, alignment, justified);
    }

    @Override
    public List<Gui> getAllGuis() {
        return master.children;
    }

    @FunctionalInterface
    public interface GuiTextFunction {
        float[] getProperty(int index, char id);
    }

}
