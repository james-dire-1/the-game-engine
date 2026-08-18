package com.james.gameplay;

import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import org.lwjgl.util.vector.Vector3f;
import templates.communication.LocalClientPacketSendEvents;

public class LocalGameLoader extends GameLoader {

    public LevelInitializer levelInitializer;

    public LocalGameLoader(Vector3f spawnPoint, String newWindowTitle) {
        super(LocalClientPacketSendEvents.get(), spawnPoint);

        this.levelInitializer = LevelInitializer.lastInstance;
        LevelInitializer.lastInstance = null;

        if (newWindowTitle != null) {
            GLFWUtilities.setWindowTitle(newWindowTitle);
        }
    }

    @Override
    protected void onGameClientClosing() {
        levelInitializer.shouldRun = false;
    }

}
