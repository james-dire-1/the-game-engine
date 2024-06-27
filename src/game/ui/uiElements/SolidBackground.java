package game.ui.uiElements;

import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.dataTypes.NormalizedPosition;
import com.james.renderEngine.ui.dataTypes.NormalizedSize;
import com.james.tools.ColorUtils;

public class SolidBackground extends Gui {

    public SolidBackground(int color) {
        super(new NormalizedPosition(0, 0), new NormalizedSize(2, 2), null);
        float[] colorArray = ColorUtils.asNormalizedRGBArray(color);
        super.setSingleColor(colorArray[0], colorArray[1], colorArray[2]);
        super.apply();
    }

}
