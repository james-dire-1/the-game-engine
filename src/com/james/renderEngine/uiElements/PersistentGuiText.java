package com.james.renderEngine.uiElements;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.textRendering.dataTypes.Line;
import com.james.renderEngine.textRendering.TextMeshCreator;

import java.util.List;

/**
 * Creates a new text gui where all the characters are represented in a single gui mesh. Thus, with
 * PersistentGuiText, a new VAO is created for every unique permutation of characters. PersistentGuiText is well
 * suited for plain text and for text that uses non-updating color rules, among other applications. If it is not
 * possible to use PersistentGuiText for a given use case, then GuiText must be used, which allows for more
 * flexibility at the cost of efficiency.
 */
public class PersistentGuiText extends AbstractGuiText {

    public PersistentGuiText(String text, FontInfo font, float fontSize, Position position) {
        super(text, font, fontSize, position);
    }

    public PersistentGuiText(List<Line> lines, FontInfo font, float fontSize, Position position) {
        super(lines, font, fontSize, position);
    }

    @Override
    public void modifyGlobalColor(float r, float g, float b) {
        mesh.setSingleColor(r, g, b);
    }

    @Override
    public void modifyGlobalScale(float globalScaleX, float globalScaleY) {
        ((ScreenSize) mesh.size).setSize(globalScaleX, globalScaleY);
    }

    @Override
    public void setVisibility(boolean visible) {
        mesh.isVisible = visible;
    }

    @Override
    public void setAlpha(float alpha) {
        mesh.alpha = alpha;
    }

    // TODO: 2025-07-22 Alpha changing is not working for text at all! Maybe it's something in the shaders causing this?
    @Override
    public void changeAlpha(float deltaAlpha) {
        mesh.alpha += deltaAlpha;
    }

    private Gui mesh;

    @Override
    public void apply() {
        List<Line> finalLines;
        if (this.lines == null) finalLines = super.getLines();
        else finalLines = this.lines;

        this.mesh = TextMeshCreator.createMesh(position, singleColor, textColorRules, text, font, fontSize, finalLines, alignment, justified);
    }

    public Gui getMesh() {
        return mesh;
    }

}
