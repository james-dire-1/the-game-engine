package com.james.main;

import com.james.main.clientSide.ClientPacketReceiveActions;
import com.james.tools.ThreadManager;
import com.james.world.PhysicalObjectType;
import com.james.world.ServerPacketSendEvents;
import com.james.world.WallTriangle;
import org.lwjgl.util.vector.Vector3f;

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
