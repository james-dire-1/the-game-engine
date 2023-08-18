package com.james.world;

import com.james.collisions.AbstractObjectHitbox;

public class ObjectHitbox extends AbstractObjectHitbox {

    public float secondsTillEndCollision = 1;
    public boolean reactWhenPushed;

    public ObjectHitbox(PhysicalObject object, float radius, boolean reactWhenPushed) {
        super(object, radius);
        this.reactWhenPushed = reactWhenPushed;
    }

}
