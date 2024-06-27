package com.james.serverSide.simulation.collisionEngine;

import com.james.common.simulation.collisionEngine.math.CommonCollisionProcedure;
import com.james.common.simulation.LevelProperties;
import com.james.serverSide.simulation.collisionEngine.hitboxes.AABBHitbox;
import com.james.serverSide.simulation.collisionEngine.hitboxes.EllipsoidHitbox;
import com.james.serverSide.simulation.objects.MovableObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Collision handler to be used server-side.
 */
public class CollisionHandler {

    public final List<EllipsoidHitbox> ellipsoidHitboxes = new ArrayList<>();
    public final List<AABBHitbox> aabbHitboxes = new ArrayList<>();

    private final LevelProperties levelProperties;

    public CollisionHandler(LevelProperties levelProperties) {
        this.levelProperties = levelProperties;
    }

    /**
     * Updates collision logic.
     * @implNote if the narrow collision test was not performed (i.e. the EllipsoidHitbox did not collide
     * with any AABBs), then the regular update() method of the MovableObject is called.
     */
    public void update() {
        for (EllipsoidHitbox ellipsoidHitbox : ellipsoidHitboxes) {
            MovableObject movableObject = ellipsoidHitbox.movableObject;
            if (movableObject.isAffectedByAABBCollisions) {
                boolean algorithmPerformed = CommonCollisionProcedure.performEntireCollisionDetectionAlgorithm(ellipsoidHitbox, aabbHitboxes, levelProperties);
                if (!algorithmPerformed) {
                    movableObject.update();
                }
            }
        }
    }

}
