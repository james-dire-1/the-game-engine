package templates.gameplay;

import com.james.serverSide.LevelInitializer;
import templates.gameplay.GameLoader;
import templates.communication.LocalClientPacketSendEvents;
import templates.communication.LocalServerPacketSendEvents;

public class LocalGameLoader extends GameLoader {

    public LevelInitializer levelInitializer;

    public LocalGameLoader() {
        super(new LocalClientPacketSendEvents());
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
