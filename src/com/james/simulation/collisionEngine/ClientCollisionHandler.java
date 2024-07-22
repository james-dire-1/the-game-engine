package com.james.simulation.collisionEngine;

import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.math.CommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.simulation.collisionEngine.hitboxes.PlayerHitbox;
import com.james.simulation.objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Collision handler to be used client-side.
 */
public class ClientCollisionHandler {

    public final PlayerHitbox playerHitbox;
    public final List<CachedAABBHitbox> cachedAABBHitboxes = new ArrayList<>();

    private final LevelProperties levelProperties;
    private final Player player;


    public ClientCollisionHandler(LevelProperties levelProperties, Player player) {
        this.levelProperties = levelProperties;
        this.player = player;
        this.playerHitbox = new PlayerHitbox(player, EllipsoidDimensions.get(1, 1, 1));
    }

    /**
     * Updates collision logic.
     * @implNote if the narrow collision test was not performed (i.e. the PlayerHitbox did not collide
     * with any AABBs), then the regular update() method of the Player is called.
     */
    public void update() {
        if (player.isAffectedByAABBCollisions) {
            boolean algorithmPerformed = CommonCollisionProcedure.performEntireCollisionDetectionAlgorithm(playerHitbox, cachedAABBHitboxes, levelProperties);
            if (!algorithmPerformed) {
                player.moveUpdate();
            }
        }
    }

}
