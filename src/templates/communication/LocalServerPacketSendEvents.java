package templates.communication;

import com.james.serverSide.LevelInitializer;
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
    public void notifyThatLevelIsReady() {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.notifyThatLevelIsReady");

        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::levelIsReadyReceived);
    }

    // TODO: 2024-06-20 This implementation is going to have to change once we add the networking eventually
    @Override
    public void sendPhysicalObjectAddedToLevel(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
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
    public void sendAABBHitboxAdded(int id, String meshPath) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalServerPacketSendEvents.sendAABBHitboxAdded " + "{id=" + id + "}");

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.aabbHitboxAddedReceived(id, meshPath);
        });
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

}
