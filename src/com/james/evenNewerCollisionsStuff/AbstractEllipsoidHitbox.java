package com.james.evenNewerCollisionsStuff;

import com.james.collisions.AbstractPhysicalObject;

public class AbstractEllipsoidHitbox {

    public final AbstractPhysicalObject object;
    public final EllipsoidDimensions dimensions;

    public AbstractEllipsoidHitbox(AbstractPhysicalObject object, EllipsoidDimensions dimensions) {
        this.object = object;
        this.dimensions = dimensions;
    }

}
