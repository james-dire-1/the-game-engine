package newStuff;

import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.hitboxes.AbstractAABBHitbox;
import com.james.common.simulation.collisionEngine.hitboxes.AbstractEllipsoidHitbox;
import com.james.common.simulation.collisionEngine.hitboxes.Ray;
import com.james.common.simulation.collisionEngine.math.PointOperations;
import com.james.common.simulation.collisionEngine.math.RSTCommonCollisionProcedure;
import com.james.common.simulation.collisionEngine.math.containers.RayTriangleInfo;
import com.james.common.tools.Mth;
import org.lwjgl.util.vector.Vector3f;

import java.util.Collection;

public class RayCollisionDefense {

    private static final float EPSILON = 0.005f;

    public static void changeMovableObjectVelocityIfNecessary(AbstractEllipsoidHitbox ellipsoidHitbox, Collection<? extends AbstractAABBHitbox> aabbHitboxes, Vector3f velocity, float secondsPerGameTick) {
        Vector3f ellipsoidRadius = ellipsoidHitbox.dimensions.radius;
        Vector3f velocityInEllipsoidSpace = PointOperations.dividePointByEllipsoidRadius(velocity, ellipsoidRadius);
        float distanceToTravelThisTick = velocityInEllipsoidSpace.length() * secondsPerGameTick;

        if (distanceToTravelThisTick < 1.0f - EPSILON)
            return;

        Vector3f position = ellipsoidHitbox.movableObject.getPosition();
        Ray ray = new Ray(position, velocity);
        RayTriangleInfo rayTriangleInfo = new RayTriangleInfo(true, false);

        RSTCommonCollisionProcedure.findClosestRayIntersectionWithTriangle(ray, -1.0f, distanceToTravelThisTick + EPSILON, aabbHitboxes, rayTriangleInfo);

        if (rayTriangleInfo.closestIntersectionPoint == null)
            return;

        Vector3f intersectionPointInEllipsoidSpace = rayTriangleInfo.closestIntersectionPoint;
        PointOperations.dividePointByEllipsoidRadius(intersectionPointInEllipsoidSpace, ellipsoidRadius);
        Vector3f normalizedVelocityInEllipsoidSpace = velocityInEllipsoidSpace.normalise(null);
        Vector3f positionInEllipsoidSpace = PointOperations.dividePointByEllipsoidRadius(position, ellipsoidRadius);

        float distance = Mth.distance(positionInEllipsoidSpace, intersectionPointInEllipsoidSpace);
        float parameter = distance - 0.75f;
        Vector3f correctedVelocityInEllipsoidSpace = Mth.multiply(normalizedVelocityInEllipsoidSpace, parameter / secondsPerGameTick);
        PointOperations.multiplyPointByEllipsoidRadiusDest(correctedVelocityInEllipsoidSpace, ellipsoidRadius);

        velocity.set(correctedVelocityInEllipsoidSpace);
    }

}
