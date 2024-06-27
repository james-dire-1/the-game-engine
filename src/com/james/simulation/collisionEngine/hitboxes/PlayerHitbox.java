package com.james.simulation.collisionEngine.hitboxes;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractEllipsoidHitbox;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import game.player.Player;

/**
 * Ellipsoid hitbox to be used client-side for the Player.
 */
public class PlayerHitbox extends AbstractEllipsoidHitbox {

    public PlayerHitbox(Player player, EllipsoidDimensions dimensions) {
        super(player, dimensions);
    }

}
