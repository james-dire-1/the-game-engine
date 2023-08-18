package com.james.main;

import com.james.collisions.AbstractObjectHitbox;

public class PlayerHitbox extends AbstractObjectHitbox {

    public final Player player;

    public PlayerHitbox(Player player, float radius) {
        super(player, radius);
        this.player = player;
    }

}
