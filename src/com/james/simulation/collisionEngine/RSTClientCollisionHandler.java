package com.james.simulation.collisionEngine;

import com.james.serverSide.simulation.collisionEngine.hitboxes.SphereHitbox;
import com.james.simulation.collisionEngine.hitboxes.CachedSphereHitbox;
import com.james.simulation.objects.Player;
import com.james.common.simulation.collisionEngine.math.RSTCommonCollisionProcedure;

import java.util.HashMap;
import java.util.Map;

public class RSTClientCollisionHandler {

    public final SphereHitbox playerSphereHitbox;
    public final Map<CachedSphereHitbox.Identifier, CachedSphereHitbox> cachedSphereHitboxes = new HashMap<>();

    public RSTClientCollisionHandler(Player player, float playerSphereHitboxRadius) {
        this.playerSphereHitbox = new SphereHitbox(player, playerSphereHitboxRadius);
    }

    public void updateSphereHitboxes() {
        RSTCommonCollisionProcedure.beRepelledByOtherSpheresForThisSphere(playerSphereHitbox, cachedSphereHitboxes.values());
    }

}
