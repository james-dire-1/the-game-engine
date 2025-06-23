package newStuff;

import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.ScreenSize;

import java.util.List;

// TODO: 2024-12-27 Note that most of these values on top are only used at initialization
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
