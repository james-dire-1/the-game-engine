package com.james.simulation.collisionEngine.hitboxes;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractAABBHitbox;
import com.james.simulation.ClientLevel;

/**
 * AABB hitbox to be used client-side.
 */
public class CachedAABBHitbox extends AbstractAABBHitbox {

    public CachedAABBHitbox(int idOfCorrespondingObject, String meshPath) {
        super(ClientLevel.get().getCachedPhysicalObject(idOfCorrespondingObject), meshPath);
    }

}
