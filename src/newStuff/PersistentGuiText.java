package newStuff;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenSize;

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

    @Override
    public void modifyGlobalColor(float r, float g, float b) {
        mesh.setSingleColor(r, g, b);
    }

    @Override
    public void modifyGlobalScale(float globalScaleX, float globalScaleY) {
        ((ScreenSize) mesh.size).setSize(globalScaleX, globalScaleY);
    }

    private Gui mesh;

    @Override
    public void apply() {
        List<Line> lines = super.getLines();
        this.mesh = TextMeshCreator.createMesh(position, singleColor, textColorRules, text, font, fontSize, lines, alignment, justified);
    }

    public Gui getMesh() {
        return mesh;
    }

}
