package com.james.simulation.collisionEngine.hitboxes;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractAABBHitbox;
import game.communication.ClientPacketReceiveActions;

/**
 * AABB hitbox to be used client-side.
 */
public class CachedAABBHitbox extends AbstractAABBHitbox {

    public CachedAABBHitbox(int idOfCorrespondingObject, String meshPath) {
        super(ClientPacketReceiveActions.cachedLocalPhysicalObjects.get(idOfCorrespondingObject), meshPath);
    }

}
