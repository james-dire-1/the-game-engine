package templates.communication;

import com.james.serverSide.LevelInitializer;
import com.james.serverSide.PlayerInfo;
import com.james.tools.ThreadManager;
import com.james.common.simulation.objects.PhysicalObjectType;
import templates.serverSide.communication.ServerPacketSendEvents;
import org.lwjgl.util.vector.Vector3f;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;

/**
 * A class containing methods that dictate what should happen when particular server events occur. In this case
 * specifically, these implemented methods dictate what should happen if the server that is running is a local
 * server (i.e. it is not an online game). In this case, we don't need to send data over a network, but rather
 * we can call the corresponding methods in ClientPacketReceiveActions directly.
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
    public void notifyUsernameSuccess(PlayerInfo playerInfo) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.notifyUsernameSuccess");

        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::usernameSuccessReceived);
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
    public void sendAABBHitboxAdded(int id, String meshPath, PlayerInfo... playerInfoArray) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendAABBHitboxAdded " + "{id=" + id + "}");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.aabbHitboxAddedReceived(id, meshPath);
        });
    }

    @Override
    public void sendConnectedPlayerAdded(int id, String username, float x, float y, float z, float rotY, PlayerInfo playerInfo) {
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

}
