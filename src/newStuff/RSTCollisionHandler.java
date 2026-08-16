package newStuff;

import com.james.common.simulation.objects.AbstractPhysicalObject;
import com.james.common.tools.Mth;
import com.james.serverSide.simulation.objects.MovableObject;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSTCollisionHandler {

    public final List<SphereHitbox> sphereHitboxes = new ArrayList<>();

    private static final Vector3f ZERO_VECTOR = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Random r = new Random();

    public void updateSphereHitboxes() {
        for (SphereHitbox firstSphereHitbox : sphereHitboxes) {
            AbstractPhysicalObject firstPhysicalObject = firstSphereHitbox.object;

            if (!(firstPhysicalObject instanceof MovableObject))
                continue;

            Vector3f netVelocity = new Vector3f();

            for (SphereHitbox secondSphereHitbox : sphereHitboxes) {
                AbstractPhysicalObject secondPhysicalObject = secondSphereHitbox.object;

                if (firstSphereHitbox.equals(secondSphereHitbox) || !(secondPhysicalObject instanceof MovableObject))
                    continue;

                Vector3f position1 = firstPhysicalObject.getPosition();
                Vector3f position2 = secondPhysicalObject.getPosition();
                Vector3f sphere2ToSphere1 = Vector3f.sub(position1, position2, null);

                float distanceGoal = firstSphereHitbox.radius + secondSphereHitbox.radius;
                float actualDistance = sphere2ToSphere1.length();
                float normalizedDistance = actualDistance / distanceGoal;

                if (normalizedDistance >= 1)
                    continue;

                if (sphere2ToSphere1.equals(ZERO_VECTOR)) {
                    sphere2ToSphere1.set(r.nextFloat() * 2.0f - 1.0f, 0, r.nextFloat() * 2.0f - 1.0f);
                }

                Vector3f normalizedSphere2ToSphere1 = sphere2ToSphere1.normalise(null);
                float normalizedPushingStrength = (normalizedDistance - 1.0f) * (normalizedDistance - 1.0f);
                Vector3f constituentVelocity = Mth.multiply(normalizedSphere2ToSphere1, normalizedPushingStrength * SphereHitbox.MAX_SPEED);
                Vector3f.add(netVelocity, constituentVelocity, netVelocity);
            }

            ((MovableObject) firstPhysicalObject).setVelocity(netVelocity.x, netVelocity.y, netVelocity.z);
        }
    }

}
