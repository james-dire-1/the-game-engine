package newStuff.rayStuffOnHold;

import com.james.simulation.ClientLevel;
import com.james.simulation.objects.CachedPhysicalObject;

import java.util.function.Consumer;

/**
 * At the moment, this is only client side.
 */
public class GeneralSphereHitbox {

    public final CachedPhysicalObject object;
    public final float radius;

    private Consumer<CachedPhysicalObject> collideListener;

    public GeneralSphereHitbox(int idOfCorrespondingObject, float radius) {
        this.object = ClientLevel.get().getCachedPhysicalObject(idOfCorrespondingObject);
        this.radius = radius;
    }

    public void setCollideListener(Consumer<CachedPhysicalObject> collideListener) {
        this.collideListener = collideListener;
    }

    public void check() {
        // todo
    }

}
