package com.james.collisions;

public class AbstractObjectHitbox {

    public final AbstractPhysicalObject object;
    public float radius;

    public AbstractObjectHitbox(AbstractPhysicalObject object, float radius) {
        this.object = object;
        this.radius = radius;
    }

}
