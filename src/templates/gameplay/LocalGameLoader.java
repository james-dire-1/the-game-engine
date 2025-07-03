package templates.gameplay;

import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import templates.settings.GLFWWindowTitles;
import templates.communication.LocalClientPacketSendEvents;
import templates.communication.LocalServerPacketSendEvents;

public class LocalGameLoader extends GameLoader {

    public LevelInitializer levelInitializer;

    public LocalGameLoader() {
        super(new LocalClientPacketSendEvents());

        GLFWUtilities.setWindowTitle(GLFWWindowTitles.DEBUG_MODE);
    }

    @Override
    protected void additionalStartupActions() {
        this.levelInitializer = new LevelInitializer("main", new LocalServerPacketSendEvents());
    }

    @Override
    protected void onGameClientClosing() {
        levelInitializer.shouldRun = false;
    }

}
