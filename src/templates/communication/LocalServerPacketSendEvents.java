package templates.communication;

import com.james.serverSide.LevelInitializer;
import templates.common.audio.Sound;
import templates.serverSide.PlayerInfo;
import com.james.tools.ThreadManager;
import templates.common.simulation.objects.PhysicalObjectType;
import templates.serverSide.communication.ServerPacketSendEvents;
import org.lwjgl.util.vector.Vector3f;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;

/**
 * A class containing methods that dictate what should happen when particular server events occur. In this case
 * specifically, these implemented methods dictate what should happen if the server that is running is a local
 * server (i.e. it is not an online game). In this case, we don't need to send data over a network, but rather
 * we can call the corresponding methods in ClientPacketReceiveActions directly.
 *
 * @implNote These methods get called from specific LevelInitializers or other server-side code, which run on
 * different threads. Thus, to run the methods of ClientPacketReceiveActions on the main thread, ThreadManager
 * is used.
 * @see ClientPacketReceiveActions
 * @see ThreadManager
 * @see LevelInitializer
 */
public class LocalServerPacketSendEvents implements ServerPacketSendEvents {

    @Override
    public void sendUsernamePrompt(PlayerInfo playerInfo) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendUsernamePrompt");

        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::usernamePromptReceived);
    }

    @Override
    public void notifyUsernameSuccess(PlayerInfo playerInfo, String username, int color, Vector3f spawnPoint) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.notifyUsernameSuccess");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.usernameSuccessReceived(username, color, spawnPoint);
        });
    }

    @Override
    public void notifyThatLevelIsReady(PlayerInfo playerInfo) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.notifyThatLevelIsReady");

        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::levelIsReadyReceived);
    }

    // TODO: 2024-06-20 This implementation is going to have to change once we add the networking eventually
    @Override
    public void sendPhysicalObjectAddedToLevel(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale, PlayerInfo... playerInfoArray) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPhysicalObjectAddedToLevel " + "{id=" + id + "} {type=" + type +"}");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectAddedReceived(id, type, position, rotation, scale);
        });
    }

    @Override
    public void sendPhysicalObjectMoved(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPhysicalObjectMoved");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectMovedReceived(id, x, y, z);
        });
    }

    @Override
    public void sendPhysicalObjectRotated(int id, float rotX, float rotY, float rotZ) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPhysicalObjectRotated");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectRotatedReceived(id, rotX, rotY, rotZ);
        });
    }

    @Override
    public void sendPhysicalObjectScaled(int id, float scale) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPhysicalObjectScaled");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectScaledReceived(id, scale);
        });
    }

    @Override
    public void sendPhysicalObjectTransformChanged(int id, Vector3f position, Vector3f rotation, float scale) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPhysicalObjectTransformChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectTransformChangedReceived(id, position, rotation, scale);
        });
    }

    @Override
    public void sendAABBHitboxAdded(int id, String meshPath, int subMeshIdentifier, PlayerInfo... playerInfoArray) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendAABBHitboxAdded " + "{id=" + id + "}");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.aabbHitboxAddedReceived(id, meshPath, subMeshIdentifier);
        });
    }

    @Override
    public void sendConnectedPlayerAdded(int id, String username, int color, float x, float y, float z, float rotY, PlayerInfo playerInfo) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendConnectedPlayerAdded " + "{id=" + id + "} to {id=" + playerInfo.getConnectedPlayer().id + "}");

        // Do nothing; this is a local game!
    }

    @Override
    public void sendConnectedPlayerTransformChanged(int id, float x, float y, float z, float rotY, PlayerInfo exceptPlayerInfo) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendConnectedPlayerMoved");

        // Do nothing; this is a local game!
    }

    @Override
    public void sendConnectedPlayerLeft(int id, PlayerInfo exceptPlayerInfo) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendConnectedPlayerLeft " + "{id=" + id + "}");

        // Do nothing; this is a local game!
    }

    @Override
    public void sendLevelSecondsPerGameTickChanged(float secondsPerGameTick) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendLevelSecondsPerGameTickChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.levelSecondsPerGameTickChangedReceived(secondsPerGameTick);
        });
    }

    @Override
    public void sendLevelGravityChanged(float x, float y, float z) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendLevelGravityChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.levelGravityChangedReceived(x, y, z);
        });
    }

    @Override
    public void confirmChatMessageReception(PlayerInfo playerInfo, int localMessageId) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.confirmChatMessageReception");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.chatMessageReceptionConfirmationReceived(localMessageId);
        });
    }

    @Override
    public void broadcastChatMessage(int playerId, String message, PlayerInfo exceptPlayerInfo) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.broadcastChatMessage");

        // Do nothing; this is a local game!
    }

    @Override
    public void broadcastSystemMessage(String message) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.broadcastSystemMessage");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.systemMessageReceived(message);
        });
    }

    @Override
    public void sendVirtualLightAddedToLevel(int id, Vector3f position, Vector3f color, Vector3f attenuation, PlayerInfo... playerInfoArray) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualLightAddedToLevel");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualLightAddedReceived(id, position, color, attenuation);
        });
    }

    @Override
    public void sendVirtualLightMoved(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualLightMoved");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualLightMovedReceived(id, x, y, z);
        });
    }

    @Override
    public void sendVirtualLightColorChanged(int id, float r, float g, float b) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualLightColorChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualLightColorChangedReceived(id, r, g, b);
        });
    }

    @Override
    public void sendVirtualLightAttenuationChanged(int id, float att1, float att2, float att3) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualLightAttenuationChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualLightAttenuationChangedReceived(id, att1, att2, att3);
        });
    }

    @Override
    public void sendVirtualLightPropertiesChanged(int id, Vector3f position, Vector3f color, Vector3f attenuation) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualLightPropertiesChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualLightPropertiesChangedReceived(id, position, color, attenuation);
        });
    }

    @Override
    public void sendVirtualDirectionalLightAddedToLevel(int id, Vector3f toLightDirection, Vector3f color, PlayerInfo... playerInfoArray) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualDirectionalLightAddedToLevel");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualDirectionalLightAddedReceived(id, toLightDirection, color);
        });
    }

    @Override
    public void sendVirtualDirectionalLightToLightDirectionChanged(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualDirectionalLightToLightDirectionChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualDirectionalLightToLightDirectionChangedReceived(id, x, y, z);
        });
    }

    @Override
    public void sendVirtualDirectionalLightColorChanged(int id, float r, float g, float b) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualDirectionalLightColorChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualDirectionalLightColorChangedReceived(id, r, g, b);
        });
    }

    @Override
    public void sendVirtualDirectionalLightPropertiesChanged(int id, Vector3f toLightDirection, Vector3f color) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendVirtualDirectionalLightPropertiesChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.virtualDirectionalLightPropertiesChangedReceived(id, toLightDirection, color);
        });
    }

    @Override
    public void sendSkyboxChanged(String name, boolean unmoving, PlayerInfo... playerInfoArray) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendSkyboxChanged");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.skyboxChangedReceived(name, unmoving);
        });
    }

    @Override
    public void sendPlaySoundAtPhysicalObject(Sound sound, int id) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPlaySoundAtPhysicalObject");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.playSoundAtPhysicalObjectReceived(sound, id);
        });
    }

    @Override
    public void sendPlaySoundAtPosition(Sound sound, Vector3f position) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPlaySoundAtPosition");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.playSoundAtPositionReceived(sound, position);
        });
    }

    @Override
    public void sendCreateSoundEmitter(int customIdentifier, Vector3f position, PlayerInfo... playerInfoArray) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendCreateSoundEmitter");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.createSoundEmitterReceived(customIdentifier, position);
        });
    }

    @Override
    public void sendDestroySoundEmitter(int customIdentifier) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendDestroySoundEmitter");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.destroySoundEmitterReceived(customIdentifier);
        });
    }

    @Override
    public void sendPlaySoundAtSoundEmitter(Sound sound, int customIdentifier) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendPlaySoundAtSoundEmitter");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.playSoundAtSoundEmitterReceived(sound, customIdentifier);
        });
    }

    @Override
    public void sendUpdatePositionOfSoundEmitter(int customIdentifier, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendUpdatePositionOfSoundEmitter");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.updatePositionOfSoundEmitterReceived(customIdentifier, x, y, z);
        });
    }

}
