package com.james.simulation.collisionEngine;

import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.math.CommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.serverSide.simulation.collisionEngine.hitboxes.EllipsoidHitbox;
import com.james.simulation.ClientLevel;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
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

    public final EllipsoidHitbox playerEllipsoidHitbox;
    public final Map<CachedAABBHitbox.Identifier, CachedAABBHitbox> cachedAABBHitboxes = new HashMap<>();

    private final Player player;

    public ClientCollisionHandler(Player player, Vector3f radius) {
        this.player = player;
        this.playerEllipsoidHitbox = new EllipsoidHitbox(player, EllipsoidDimensions.get(radius.x, radius.y, radius.z));
    }

    /**
     * Updates collision logic.
     *
     * @implNote if the narrow collision test was not performed (i.e. the PlayerHitbox did not collide
     * with any AABBs), then the regular update() method of the Player is called.
     */
    public void update() {
        if (player.canCollideWithTriangles) {
            boolean algorithmPerformed = CommonCollisionProcedure.performEntireCollisionDetectionAlgorithm(playerEllipsoidHitbox, cachedAABBHitboxes.values(), ClientLevel.get(), DebugScreen.collisionsAccumulator);
            if (!algorithmPerformed) {
                player.setPositionBasedOnVelocity();
            }
        }
    }

}
