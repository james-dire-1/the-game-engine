package com.james.world;

import org.lwjgl.util.vector.Vector3f;

public interface ServerPacketSendEvents {

    void sendPhysicalObjectAddedToLevel(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale);
    void sendPhysicalObjectMoved(int id, float x, float y, float z);
    void sendObjectHitboxAdded(int idOfCorrespondingObject, float radius);
    void sendWallTrianglesAdded(WallTriangle[] wallTriangles);

}
