package templates.gameplay;

import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import org.lwjgl.util.vector.Vector3f;
import templates.settings.GLFWWindowTitles;
import templates.communication.LocalClientPacketSendEvents;

public class LocalGameLoader extends GameLoader {

    public LevelInitializer levelInitializer;

    public LocalGameLoader(Vector3f spawnPoint) {
        super(LocalClientPacketSendEvents.get(), spawnPoint);

        this.levelInitializer = LevelInitializer.lastInstance;
        LevelInitializer.lastInstance = null;

        GLFWUtilities.setWindowTitle(GLFWWindowTitles.DEBUG_MODE);
    }

    @Override
    protected void onGameClientClosing() {
        levelInitializer.shouldRun = false;
    }

}
