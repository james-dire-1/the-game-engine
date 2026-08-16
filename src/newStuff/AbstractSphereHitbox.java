package newStuff;

import com.james.common.simulation.objects.AbstractPhysicalObject;

public abstract class AbstractSphereHitbox {

    public static final float MAX_SPEED = 10.0f;

    public final AbstractPhysicalObject object;
    public final float radius;

    public AbstractSphereHitbox(AbstractPhysicalObject object, float radius) {
        this.object = object;
        this.radius = radius;
    }

}
