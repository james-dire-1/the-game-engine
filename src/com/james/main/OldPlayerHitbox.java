package com.james.main;

import com.james.collisions.AbstractObjectHitbox;

public class OldPlayerHitbox extends AbstractObjectHitbox {

    public final Player player;

    public OldPlayerHitbox(Player player, float radius) {
        super(player, radius);
        this.player = player;
    }

}
