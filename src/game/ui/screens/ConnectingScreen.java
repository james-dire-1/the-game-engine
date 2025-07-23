package game.ui.screens;

import com.james.networking.Client;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.uiElements.GuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.simulation.ClientLevel;
import com.james.tools.ThreadManager;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import newStuff.ChatScreen;
import templates.gameplay.OnlineGameLoader;
import com.james.renderEngine.textRendering.TextAlignment;
import templates.common.networking.PacketType;

import java.io.IOException;

public class ConnectingScreen extends Screen {

    public ConnectingScreen(String host) {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        GuiText text = new GuiText("Connecting to remote server...", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.CENTER));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.apply();
        super.addGuis(text.getAllGuis());

        connectToServer(host);
    }

    private void connectToServer(String host) {
        new Client(host, 6789) {
            @Override
            public void preConnectTasks() {
                setReadListener(PacketType.USERNAME_PROMPT, OnlineGameLoader::usernamePromptReceived);
                setReadListener(PacketType.USERNAME_SUCCESS, OnlineGameLoader::usernameSuccessReceived);
                setReadListener(PacketType.LEVEL_IS_READY, OnlineGameLoader::levelIsReadyReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_ADDED_TO_LEVEL, OnlineGameLoader::physicalObjectAddedReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_MOVED, OnlineGameLoader::physicalObjectMovedReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_ROTATED, OnlineGameLoader::physicalObjectRotatedReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_SCALED, OnlineGameLoader::physicalObjectScaledReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_TRANSFORM_CHANGED, OnlineGameLoader::physicalObjectTransformChangedReceived);
                setReadListener(PacketType.AABB_HITBOX_ADDED, OnlineGameLoader::aabbHitboxAddedReceived);
                setReadListener(PacketType.CONNECTED_PLAYER_ADDED, OnlineGameLoader::connectedPlayerAddedReceived);
                setReadListener(PacketType.CONNECTED_PLAYER_TRANSFORM_CHANGED, OnlineGameLoader::connectedPlayerTransformChangedReceived);
                setReadListener(PacketType.CONNECTED_PLAYER_LEFT, OnlineGameLoader::connectedPlayerLeftReceived);
                setReadListener(PacketType.LEVEL_SECONDS_PER_GAME_TICK_CHANGED, OnlineGameLoader::levelSecondsPerGameTickChangedReceived);
                setReadListener(PacketType.LEVEL_GRAVITY_CHANGED, OnlineGameLoader::levelGravityChangedReceived);
                setDisconnectListener(OnlineGameLoader::onConnectionException);
            }

            @Override
            public void onSuccessfulConnection() {
            }

            @Override
            public void exceptionInConstruction(IOException e) {
                ThreadManager.executeOnMainThread(() -> {
                    ConnectingScreen.super.markForDeletion();
                    UiHandler.screens.add(new FailedToConnectScreen(host, e));
                });
            }
        };
    }

    @Override
    public void update() {
        ClientLevel level = ClientLevel.get();

        if (level != null && level.isReady) {
            super.markForDeletion();
            queueScreenForAddition(new ChatScreen());

            GLFWUtilities.lockCursor(true);
        }

        super.update();
    }
}
