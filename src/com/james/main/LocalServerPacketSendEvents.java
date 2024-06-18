package com.james.main;

import com.james.main.clientSide.ClientPacketReceiveActions;
import com.james.tools.ThreadManager;
import com.james.world.PhysicalObjectType;
import com.james.world.ServerPacketSendEvents;
import com.james.world.WallTriangle;
import org.lwjgl.util.vector.Vector3f;

/**
 * A class containing methods that dictate what should happen when particular server events occur. In this case
 * specifically, these implemented methods dictate what should happen if the server that is running is a local
 * server (i.e. it is not an online game). In this case, we don't need to send data over a network, but rather
 * we can call the corresponding methods in ClientPacketReceiveActions directly.
 * @implNote These methods get called from specific LevelInitializers or other server side code, which run on
 * different threads. Thus, to run the methods of ClientPacketReceiveActions on the main thread, ThreadManager
 * is used.
 * @see ClientPacketReceiveActions
 * @see ThreadManager
 * @see com.james.world.LevelInitializer
 */
public class LocalServerPacketSendEvents implements ServerPacketSendEvents {

    @Override
    public void sendPhysicalObjectAddedToLevel(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectAddedReceived(id, type, position, rotation, scale);
        });
    }

    @Override
    public void sendPhysicalObjectMoved(int id, float x, float y, float z) {
        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectMovedReceived(id, x, y, z);
        });
    }

    @Override
    public void sendAABBHitboxAdded(int id, String meshPath) {
        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.aabbHitboxAddedReceived(id, meshPath);
        });
    }

    // Outdated code. Will be removed in the future.
    @Override
    public void sendObjectHitboxAdded(int idOfCorrespondingObject, float radius) {
        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.objectHitboxAddedReceived(idOfCorrespondingObject, radius);
        });
    }

    @Override
    public void sendWallTrianglesAdded(WallTriangle[] wallTriangles) {
        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.wallTrianglesAddedReceived(wallTriangles);
        });
    }

}
