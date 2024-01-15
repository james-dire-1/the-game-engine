package com.james.main.clientSide;

import com.james.collisions.AbstractObjectHitbox;

/**
 * Exactly like ObjectHitbox, but for client side. The CachedObjectHitbox doesn't include the variables
 * secondsTillEndCollision and reactWhenPushed which are in the regular ObjectHitbox class, since those are
 * things relating to collision response that should only be handled on the server side. However, in the
 * future, I may change my mind and decide to also send this info to clients as well, since it could make
 * for smoother client predictions.
 * On the other hand, WallTriangles and Triangles do not have a class dedicated for use on the client side,
 * as they use the same fields on both the client and the server.
 * @see com.james.world.ObjectHitbox
 * @see com.james.world.WallTriangle
 */
public class CachedObjectHitbox extends AbstractObjectHitbox {

    public CachedObjectHitbox(int idOfCorrespondingObject, float radius) {
        super(ClientPacketReceiveActions.cachedLocalPhysicalObjects.get(idOfCorrespondingObject), radius);
    }

}
