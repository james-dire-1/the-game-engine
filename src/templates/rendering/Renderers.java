package templates.rendering;

import com.james.renderEngine.rendering.models.BasicRenderer;
import com.james.renderEngine.rendering.models.ColorModelRenderer;
import com.james.renderEngine.rendering.models.FlatRenderer;
import com.james.renderEngine.rendering.models.TexturedModelRenderer;
import com.james.renderEngine.rendering.models.TextureBlendModelRenderer;

public class Renderers {

    public static BasicRenderer basicRenderer;
    public static FlatRenderer flatRenderer;
    public static TexturedModelRenderer texturedModelRenderer;
    public static ColorModelRenderer colorModelRenderer;
    public static TextureBlendModelRenderer textureBlendModelRenderer;

    public static void init() {
        basicRenderer = new BasicRenderer();
        flatRenderer = new FlatRenderer();
        texturedModelRenderer = new TexturedModelRenderer();
        colorModelRenderer = new ColorModelRenderer();
        textureBlendModelRenderer = new TextureBlendModelRenderer();
    }

}
