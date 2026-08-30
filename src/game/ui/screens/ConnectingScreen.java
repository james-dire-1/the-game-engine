package game.ui.screens;

import com.james.networking.Client;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import com.james.simulation.ClientLevel;
import com.james.tools.ThreadManager;
import game.main.Main;
import game.ui.uiElements.SolidBackground;
import com.james.gameplay.OnlineGameLoader;
import com.james.renderEngine.textRendering.TextAlignment;
import templates.common.networking.PacketType;

import java.io.IOException;

public class ConnectingScreen extends Screen {

    public ConnectingScreen(String host) {
        SolidBackground background = new SolidBackground(0xdddddd);
        super.addGui(background);

        PersistentGuiText text = new PersistentGuiText("Connecting to remote server...", Main.rowdies, 0.3f, new AnchoredPosition(AnchorPoint.CENTER));
        text.setAlignment(TextAlignment.CENTER_ALIGNED);
        text.apply();
        super.addGui(text.getMesh());

        LevelInitializer.isOnlineGame = true;
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
                setReadListener(PacketType.PHYSICAL_OBJECT_REMOVED_FROM_LEVEL, OnlineGameLoader::physicalObjectRemovedReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_MOVED, OnlineGameLoader::physicalObjectMovedReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_ROTATED, OnlineGameLoader::physicalObjectRotatedReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_SCALED, OnlineGameLoader::physicalObjectScaledReceived);
                setReadListener(PacketType.PHYSICAL_OBJECT_TRANSFORM_CHANGED, OnlineGameLoader::physicalObjectTransformChangedReceived);
                setReadListener(PacketType.AABB_HITBOX_ADDED, OnlineGameLoader::aabbHitboxAddedReceived);
                setReadListener(PacketType.AABB_HITBOX_REMOVED, OnlineGameLoader::aabbHitboxRemovedReceived);
                setReadListener(PacketType.SPHERE_HITBOX_ADDED, OnlineGameLoader::sphereHitboxAddedReceived);
                setReadListener(PacketType.SPHERE_HITBOX_REMOVED, OnlineGameLoader::sphereHitboxRemovedReceived);
                setReadListener(PacketType.CONNECTED_PLAYER_ADDED, OnlineGameLoader::connectedPlayerAddedReceived);
                setReadListener(PacketType.CONNECTED_PLAYER_TRANSFORM_CHANGED, OnlineGameLoader::connectedPlayerTransformChangedReceived);
                setReadListener(PacketType.CONNECTED_PLAYER_LEFT, OnlineGameLoader::connectedPlayerLeftReceived);
                setReadListener(PacketType.LEVEL_SECONDS_PER_GAME_TICK_CHANGED, OnlineGameLoader::levelSecondsPerGameTickChangedReceived);
                setReadListener(PacketType.LEVEL_GRAVITY_CHANGED, OnlineGameLoader::levelGravityChangedReceived);
                setReadListener(PacketType.CONFIRM_CHAT_MESSAGE_RECEPTION, OnlineGameLoader::chatMessageReceptionConfirmationReceived);
                setReadListener(PacketType.BROADCASTING_CHAT_MESSAGE, OnlineGameLoader::chatMessageReceived);
                setReadListener(PacketType.BROADCASTING_SYSTEM_MESSAGE, OnlineGameLoader::systemMessageReceived);
                setReadListener(PacketType.VIRTUAL_LIGHT_ADDED, OnlineGameLoader::virtualLightAddedReceived);
                setReadListener(PacketType.VIRTUAL_LIGHT_REMOVED, OnlineGameLoader::virtualLightRemovedReceived);
                setReadListener(PacketType.VIRTUAL_LIGHT_MOVED, OnlineGameLoader::virtualLightMovedReceived);
                setReadListener(PacketType.VIRTUAL_LIGHT_COLOR_CHANGED, OnlineGameLoader::virtualLightColorChangedReceived);
                setReadListener(PacketType.VIRTUAL_LIGHT_ATTENUATION_CHANGED, OnlineGameLoader::virtualLightAttenuationChangedReceived);
                setReadListener(PacketType.VIRTUAL_LIGHT_PROPERTIES_CHANGED, OnlineGameLoader::virtualLightPropertiesChangedReceived);
                setReadListener(PacketType.VIRTUAL_DIRECTIONAL_LIGHT_ADDED, OnlineGameLoader::virtualDirectionalLightAddedReceived);
                setReadListener(PacketType.VIRTUAL_DIRECTIONAL_LIGHT_REMOVED, OnlineGameLoader::virtualDirectionalLightRemovedReceived);
                setReadListener(PacketType.VIRTUAL_DIRECTIONAL_LIGHT_DIRECTION_CHANGED, OnlineGameLoader::virtualDirectionalLightToLightDirectionChangedReceived);
                setReadListener(PacketType.VIRTUAL_DIRECTIONAL_LIGHT_COLOR_CHANGED, OnlineGameLoader::virtualDirectionalLightColorChangedReceived);
                setReadListener(PacketType.VIRTUAL_DIRECTIONAL_LIGHT_PROPERTIES_CHANGED, OnlineGameLoader::virtualDirectionalLightPropertiesChangedReceived);
                setReadListener(PacketType.SKYBOX_CHANGED, OnlineGameLoader::skyboxChangedReceived);
                setReadListener(PacketType.PLAY_SOUND_AT_PHYSICAL_OBJECT, OnlineGameLoader::playSoundAtPhysicalObjectReceived);
                setReadListener(PacketType.PLAY_SOUND_AT_POSITION, OnlineGameLoader::playSoundAtPositionReceived);
                setReadListener(PacketType.CREATE_SOUND_EMITTER, OnlineGameLoader::createSoundEmitterReceived);
                setReadListener(PacketType.DESTROY_SOUND_EMITTER, OnlineGameLoader::destroySoundEmitterReceived);
                setReadListener(PacketType.PLAY_SOUND_AT_SOUND_EMITTER, OnlineGameLoader::playSoundAtSoundEmitterReceived);
                setReadListener(PacketType.UPDATE_POSITION_OF_SOUND_EMITTER, OnlineGameLoader::updatePositionOfSoundEmitterReceived);
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
