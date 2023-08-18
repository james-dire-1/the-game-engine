package com.james.main.clientSide;

import com.james.collisions.AbstractObjectHitbox;

public class CachedObjectHitbox extends AbstractObjectHitbox {

    public CachedObjectHitbox(int idOfCorrespondingObject, float radius) {
        super(ClientPacketReceiveActions.cachedLocalPhysicalObjects.get(idOfCorrespondingObject), radius);
    }

}
