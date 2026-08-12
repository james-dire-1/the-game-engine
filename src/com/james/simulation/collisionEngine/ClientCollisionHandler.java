package com.james.simulation.collisionEngine;

import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.math.CommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.simulation.collisionEngine.hitboxes.PlayerHitbox;
import com.james.simulation.objects.Player;
import game.ui.screens.DebugScreen;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * Collision handler to be used client-side. This collision handler is used for ellipsoid vs triangle
 * collisions.
 */
public class ClientCollisionHandler {

    public final PlayerHitbox playerHitbox;
    public final Map<CachedAABBHitbox.Identifier, CachedAABBHitbox> cachedAABBHitboxes = new HashMap<>();

    private final LevelProperties levelProperties;
    private final Player player;

    public ClientCollisionHandler(LevelProperties levelProperties, Player player, Vector3f radius) {
        this.levelProperties = levelProperties;
        this.player = player;
        this.playerHitbox = new PlayerHitbox(player, EllipsoidDimensions.get(radius.x, radius.y, radius.z));
    }

    /**
     * Updates collision logic.
     *
     * @implNote if the narrow collision test was not performed (i.e. the PlayerHitbox did not collide
     * with any AABBs), then the regular update() method of the Player is called.
     */
    public void update() {
        if (player.isAffectedByAABBCollisions) {
            boolean algorithmPerformed = CommonCollisionProcedure.performEntireCollisionDetectionAlgorithm(playerHitbox, cachedAABBHitboxes.values(), levelProperties, DebugScreen.collisionsAccumulator);
            if (!algorithmPerformed) {
                player.moveUpdate();
            }
        }
    }

}
