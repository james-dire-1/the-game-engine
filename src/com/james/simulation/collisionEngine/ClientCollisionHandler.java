package com.james.simulation.collisionEngine;

import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.math.CommonCollisionProcedure;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.simulation.collisionEngine.hitboxes.PlayerHitbox;
import game.player.Player;
import game.communication.ClientPacketReceiveActions;

import java.util.List;

/**
 * Collision handler to be used client-side.
 */
public class ClientCollisionHandler {

    /**
     * Updates collision logic.
     * @implNote if the narrow collision test was not performed (i.e. the PlayerHitbox did not collide
     * with any AABBs), then the regular update() method of the Player is called.
     */
    public static void update() {
        PlayerHitbox playerHitbox = ClientPacketReceiveActions.playerHitbox;
        List<CachedAABBHitbox> cachedAABBHitboxes = ClientPacketReceiveActions.cachedLocalAABBHitboxes;
        LevelProperties levelProperties = ClientPacketReceiveActions.levelProperties;

        Player player = ClientPacketReceiveActions.player;
        if (player.isAffectedByAABBCollisions) {
            boolean algorithmPerformed = CommonCollisionProcedure.performEntireCollisionDetectionAlgorithm(playerHitbox, cachedAABBHitboxes, levelProperties);
            if (!algorithmPerformed) {
                player.update();
            }
        }
    }

}
