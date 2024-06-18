package com.james.world;

import org.lwjgl.util.vector.Vector3f;

/**
 * Various unimplemented methods for things that should be done when particular events are triggered on
 * the server. Implementation of these methods depends on whether the server is online (i.e.
 * data needs to be sent to the client), or local (i.e. client methods can be called directly).
 */
public interface ServerPacketSendEvents {

    void sendPhysicalObjectAddedToLevel(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale);
    void sendPhysicalObjectMoved(int id, float x, float y, float z);
    void sendAABBHitboxAdded(int id, String meshPath);

    // Old outdated collision stuff. Will be removed in the future
    void sendObjectHitboxAdded(int idOfCorrespondingObject, float radius);
    void sendWallTrianglesAdded(WallTriangle[] wallTriangles);

}
