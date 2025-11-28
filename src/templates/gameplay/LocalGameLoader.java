package templates.gameplay;

import com.james.common.tools.Mth;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import com.james.tools.MousePicker;
import com.james.tools.RenderingMath;
import org.lwjgl.util.vector.Vector3f;
import templates.rendering.ModelBank;
import templates.settings.GLFWWindowTitles;
import templates.communication.LocalClientPacketSendEvents;

public class LocalGameLoader extends GameLoader {

    public LevelInitializer levelInitializer;

    private final MousePicker mousePicker = new MousePicker(focusCamera);
    private final GameObject gameObject;

    public LocalGameLoader() {
        super(LocalClientPacketSendEvents.get());

        this.levelInitializer = LevelInitializer.lastInstance;
        LevelInitializer.lastInstance = null;

        GLFWUtilities.setWindowTitle(GLFWWindowTitles.DEBUG_MODE);

        gameObject = new GameObject(ModelBank.getStall(), new Vector3f(), new Vector3f(), 1);
        batchedGameObjectsList.addGameObject(gameObject);
    }

    @Override
    public void update() {
        super.update();

//        mousePicker.update();
//        Vector3f outwardRay = Mth.multiply(mousePicker.getCurrentRay(), 20);

//        Vector3f result = Vector3f.add(playerHandler.getGameObject().getPosition(), outwardRay, null);
//        Vector3f result = Vector3f.add(playerHandler.getGameObject().getPosition(), outwardRay, null);

//        gameObject.setPosition(result.x, result.y, result.z);
    }

    @Override
    protected void onGameClientClosing() {
        levelInitializer.shouldRun = false;
    }

}
