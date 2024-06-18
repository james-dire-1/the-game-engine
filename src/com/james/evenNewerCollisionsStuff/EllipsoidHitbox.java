package com.james.evenNewerCollisionsStuff;

import com.james.world.MovableObject;

public class EllipsoidHitbox extends AbstractEllipsoidHitbox {
    // TODO: 2024-06-16 This implementation is really weird
    public final MovableObject movableObject;

    public EllipsoidHitbox(MovableObject movableObject, EllipsoidDimensions dimensions) {
        super(movableObject, dimensions);
        this.movableObject = movableObject;
    }

}
