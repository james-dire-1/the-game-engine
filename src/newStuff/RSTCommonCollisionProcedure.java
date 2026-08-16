package newStuff;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractAABBHitbox;
import com.james.common.simulation.collisionEngine.math.CollisionMath;
import com.james.common.simulation.collisionEngine.math.Plane;
import com.james.common.simulation.collisionEngine.math.PointOperations;
import com.james.common.simulation.collisionEngine.math.Triangle;
import com.james.common.simulation.collisionEngine.prep.ModelMesh;
import com.james.common.tools.Mth;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RSTCommonCollisionProcedure {

    public static void findClosestRayIntersectionWithSphere(Ray ray, float detectionRadius, Collection<? extends AbstractSphereHitbox> sphereHitboxes, RaySphereInfo raySphereInfo) {
        boolean interestedInIntersectionPoint = raySphereInfo.interestedInIntersectionPoint;
        boolean interestedInCollidedHitbox = raySphereInfo.interestedInCollidedHitbox;
        List<Vector3f> intersectionPoints = new ArrayList<>();
        List<AbstractSphereHitbox> collidedHitboxes = null;

        if (interestedInCollidedHitbox) {
            collidedHitboxes = new ArrayList<>();
        }

        findAllRayIntersectionsWithSpheres(ray, detectionRadius, sphereHitboxes, intersectionPoints, collidedHitboxes);

        Vector3f closestIntersectionPoint = null;
        AbstractSphereHitbox closestCollidedHitbox = null;
        float smallestDistanceSquared = Float.MAX_VALUE;

        for (int i = 0; i < intersectionPoints.size(); i++) {
            Vector3f intersectionPoint = intersectionPoints.get(i);
            float distanceSquared = Mth.squaredDistance(ray.origin, intersectionPoint);

            if (distanceSquared < smallestDistanceSquared) {
                closestIntersectionPoint = intersectionPoint;
                smallestDistanceSquared = distanceSquared;

                if (interestedInCollidedHitbox) {
                    closestCollidedHitbox = collidedHitboxes.get(i);
                }
            }
        }

        if (interestedInIntersectionPoint) {
            raySphereInfo.closestIntersectionPoint = closestIntersectionPoint;
        }
        if (interestedInCollidedHitbox) {
            raySphereInfo.closestCollidedHitbox = closestCollidedHitbox;
        }
    }

    public static void findAllRayIntersectionsWithSpheres(Ray ray, float detectionRadius, Collection<? extends AbstractSphereHitbox> sphereHitboxes, List<Vector3f> intersectionPoints, List<AbstractSphereHitbox> collidedHitboxes) {
        boolean interestedInIntersectionPoints = intersectionPoints != null;
        boolean interestedInCollidedHitboxes = collidedHitboxes != null;
        float detectionRadiusSquared = detectionRadius * detectionRadius;

        for (AbstractSphereHitbox sphereHitbox : sphereHitboxes) {
            if (Mth.squaredDistance(ray.origin, sphereHitbox.object.getPosition()) > detectionRadiusSquared  && detectionRadius > 0)
                continue;

            Vector3f intersectionPoint = null;
            if (interestedInIntersectionPoints) {
                intersectionPoint = new Vector3f();
            }

            boolean inSphere = RSTCollisionMath.rayAndSphereIntersection(ray, sphereHitbox, intersectionPoint);

            if (inSphere) {
                if (interestedInIntersectionPoints) {
                    intersectionPoints.add(intersectionPoint);
                }

                if (interestedInCollidedHitboxes) {
                    collidedHitboxes.add(sphereHitbox);
                }
            }
        }
    }

    public static void findClosestRayIntersectionWithTriangle(Ray ray, float detectionRadius, float rayLength, Collection<? extends AbstractAABBHitbox> aabbHitboxes, RayTriangleInfo rayTriangleInfo) {
        boolean interestedInIntersectionPoint = rayTriangleInfo.interestedInIntersectionPoint;
        boolean interestedInCollidedHitbox = rayTriangleInfo.interestedInCollidedHitbox;
        List<Vector3f> intersectionPoints = new ArrayList<>();
        List<AbstractAABBHitbox> collidedHitboxes = null;

        if (interestedInCollidedHitbox) {
            collidedHitboxes = new ArrayList<>();
        }

        findAllRayIntersectionsWithTriangles(ray, detectionRadius, rayLength, aabbHitboxes, intersectionPoints, collidedHitboxes);

        Vector3f closestIntersectionPoint = null;
        AbstractAABBHitbox closestCollidedHitbox = null;
        float smallestDistanceSquared = Float.MAX_VALUE;

        for (int i = 0; i < intersectionPoints.size(); i++) {
            Vector3f intersectionPoint = intersectionPoints.get(i);
            float distanceSquared = Mth.squaredDistance(ray.origin, intersectionPoint);

            if (distanceSquared < smallestDistanceSquared) {
                closestIntersectionPoint = intersectionPoint;
                smallestDistanceSquared = distanceSquared;

                if (interestedInCollidedHitbox) {
                    closestCollidedHitbox = collidedHitboxes.get(i);
                }
            }
        }

        if (interestedInIntersectionPoint) {
            rayTriangleInfo.closestIntersectionPoint = closestIntersectionPoint;
        }
        if (interestedInCollidedHitbox) {
            rayTriangleInfo.closestCollidedHitbox = closestCollidedHitbox;
        }
    }

    public static void findAllRayIntersectionsWithTriangles(Ray ray, float detectionRadius, float rayLength, Collection<? extends AbstractAABBHitbox> aabbHitboxes, List<Vector3f> intersectionPoints, List<AbstractAABBHitbox> collidedHitboxes) {
        boolean interestedInIntersectionPoints = intersectionPoints != null;
        boolean interestedInCollidedHitboxes = collidedHitboxes != null;
        float detectionRadiusSquared = detectionRadius * detectionRadius;

        Vector3f rayStart = ray.origin;
        Vector3f rayEnd = Vector3f.add(ray.origin, Mth.multiply(ray.direction, rayLength), null);

        for (AbstractAABBHitbox aabbHitbox : aabbHitboxes) {
            Vector3f objectPosition = aabbHitbox.object.getPosition();

            if (Mth.squaredDistance(ray.origin, objectPosition) > detectionRadiusSquared && detectionRadius > 0)
                continue;

            boolean inAABB = RSTCollisionMath.rayAndAABBIntersection(rayStart, rayEnd, aabbHitbox);

            if (inAABB) {
                Triangle[] trianglesInLocalSpace = aabbHitbox.mesh.triangles;
                Triangle[] trianglesInWorldSpace = ModelMesh.performOperationOnAllTriangles(trianglesInLocalSpace, PointOperations::addObjectPositionToPoint, objectPosition);

                for (Triangle triangle : trianglesInWorldSpace) {
                    Vector3f p1 = triangle.points[0];
                    Vector3f p2 = triangle.points[1];
                    Vector3f p3 = triangle.points[2];

                    Plane trianglePlane = new Plane(p1, p2, p3);
                    Vector3f possibleIntersectionPoint = RSTCollisionMath.rayAndPlaneIntersection(ray, trianglePlane);

                    if (possibleIntersectionPoint == null)
                        continue;

                    boolean inTriangle = CollisionMath.pointInTriangle(possibleIntersectionPoint, p1, p2, p3);

                    if (inTriangle) {
                        if (interestedInIntersectionPoints) {
                            intersectionPoints.add(possibleIntersectionPoint);
                        }
                        if (interestedInCollidedHitboxes) {
                            collidedHitboxes.add(aabbHitbox);
                        }
                    }
                }
            }
        }
    }

}
