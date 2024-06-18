package com.james.evenNewerCollisionsStuff;

import com.james.math.Mth;
import com.james.world.Level;
import com.james.world.MovableObject;
import com.james.world.Triangle;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class CollisionHandler {

    public final List<EllipsoidHitbox> ellipsoidHitboxes = new ArrayList<>();
    public final List<AABBHitbox> aabbHitboxes = new ArrayList<>();

    private final Level level;

    private static final float VERY_CLOSE_DISTANCE = 0.005f;
    private static final float VERY_CLOSE_DISTANCE_SQUARED = VERY_CLOSE_DISTANCE*VERY_CLOSE_DISTANCE;
    private static final int MAX_RECURSION_COUNT = 5;

    public CollisionHandler(Level level) {
        this.level = level;
    }

    public void update() {
        for (EllipsoidHitbox ellipsoidHitbox : ellipsoidHitboxes) {
            for (AABBHitbox aabbHitbox : aabbHitboxes) {
                // TODO: 2024-06-16 Write the condition required for collision here
                Vector3f objectPosition = aabbHitbox.object.getPosition();
                ModelMesh meshInEllipsoidLocalSpace = ellipsoidHitbox.dimensions.modelMeshMap.get(aabbHitbox.meshPath);
                Triangle[] trianglesInEllipsoidLocalSpace = meshInEllipsoidLocalSpace.triangles;
                // TODO: 2024-06-18 For efficiency's sake, we should definitely cache the
                //  trianglesInEllipsoidWorldSpace after they are created, but how?!!   :O
                Triangle[] trianglesInEllipsoidWorldSpace = ModelMesh.performOperationOnAllTriangles(trianglesInEllipsoidLocalSpace, PointOperations::addObjectPositionToPoint, objectPosition);

                Vector3f ellipsoidHitboxRadius = ellipsoidHitbox.dimensions.radius;

                // Velocity (AKA displacementThisTick) vector is the MovableObject's velocity
                MovableObject movableObject = ellipsoidHitbox.movableObject;
                Vector3f basePoint = movableObject.getPosition();
                basePoint = PointOperations.dividePointByEllipsoidRadius(basePoint, ellipsoidHitboxRadius);

                Vector3f displacementThisTick = Mth.multiply(movableObject.getVelocity(), level.secondsPerGameTick);
                displacementThisTick = PointOperations.dividePointByEllipsoidRadius(displacementThisTick, ellipsoidHitboxRadius);

                int recursionCount = 0;
                Vector3f finalPosition = collisionDetectionAndResponse(trianglesInEllipsoidWorldSpace, basePoint, displacementThisTick, recursionCount);

                // Velocity (AKA displacementThisTick) vector is the gravity vector
                displacementThisTick = Mth.multiply(level.gravity, level.secondsPerGameTick);
                displacementThisTick = PointOperations.dividePointByEllipsoidRadius(displacementThisTick, ellipsoidHitboxRadius);

                recursionCount = 0;
                finalPosition = collisionDetectionAndResponse(trianglesInEllipsoidWorldSpace, finalPosition, displacementThisTick, recursionCount);

                // Convert result back into R3
                finalPosition = PointOperations.multiplyPointByEllipsoidRadius(finalPosition, ellipsoidHitboxRadius);
                movableObject.setPosition(finalPosition.x, finalPosition.y, finalPosition.z);
            }
        }
    }

    // TODO: 2024-06-18 Change return type if needed
    // TODO: 2024-06-18 How many recursions are actually happening here?
    private static Vector3f collisionDetectionAndResponse(Triangle[] trianglesInEllipsoidWorldSpace, Vector3f position, Vector3f velocity, int recursionCount) {
        if (recursionCount > MAX_RECURSION_COUNT)
            return position;

        CollisionInfo collisionInfo = new CollisionInfo();
        for (Triangle triangle : trianglesInEllipsoidWorldSpace) {
            // The weird order is to change from counterclockwise to clockwise
            Vector3f p1 = triangle.points[0];
            Vector3f p2 = triangle.points[2];
            Vector3f p3 = triangle.points[1];

            performCollisionDetectionWithTriangle(position, velocity, p1, p2, p3, collisionInfo);
        }

        Vector3f destinationPoint = Vector3f.add(position, velocity, null);
        if (!collisionInfo.foundCollision)
            return destinationPoint;

        Vector3f newBasePoint = new Vector3f(position);

        if (collisionInfo.intersectionDistance >= VERY_CLOSE_DISTANCE) {
            Vector3f v = new Vector3f(velocity);
            v.normalise(v);
            v.scale(collisionInfo.intersectionDistance - VERY_CLOSE_DISTANCE);
            newBasePoint = Vector3f.add(position, v, null);
            v.normalise(v);
            Vector3f.sub(collisionInfo.intersectionPoint, Mth.multiply(v, VERY_CLOSE_DISTANCE), collisionInfo.intersectionPoint);
        }

        // TODO: 2024-06-18 Is a new instance of Vector3f necessary here?
        Vector3f slidingPlaneOrigin = new Vector3f(collisionInfo.intersectionPoint);
        Vector3f slidingPlaneNormal = Vector3f.sub(newBasePoint, collisionInfo.intersectionPoint, null);
        slidingPlaneNormal.normalise(slidingPlaneNormal);
        Plane slidingPlane = new Plane(slidingPlaneOrigin, slidingPlaneNormal);

        Vector3f newDestinationPoint = Vector3f.sub(destinationPoint, Mth.multiply(slidingPlaneNormal, (float)slidingPlane.signedDistanceTo(destinationPoint)), null);
        Vector3f newVelocityVector = Vector3f.sub(newDestinationPoint, collisionInfo.intersectionPoint, null);

        if (newVelocityVector.lengthSquared() < VERY_CLOSE_DISTANCE_SQUARED)
            return newBasePoint;

        recursionCount++;

        return collisionDetectionAndResponse(trianglesInEllipsoidWorldSpace, newBasePoint, newVelocityVector, recursionCount);
    }

    private static void performCollisionDetectionWithTriangle(Vector3f basePoint, Vector3f velocity, Vector3f p1, Vector3f p2, Vector3f p3, CollisionInfo collisionInfo) {
        Vector3f normalizedVelocity = velocity.normalise(null);
        Plane trianglePlane = new Plane(p1, p2, p3);

        if (!trianglePlane.isFrontFacingTo(normalizedVelocity)) return;

        double t0, t1;
        boolean embeddedInPlane = false;

        double signedDistanceToTrianglePlane = trianglePlane.signedDistanceTo(basePoint);

        float normalDotVelocity = Vector3f.dot(trianglePlane.normal, velocity);
        if (normalDotVelocity == 0.0f) {
            if (Math.abs(signedDistanceToTrianglePlane) >= 1.0f) return;

            embeddedInPlane = true;
            t0 = 0.0;
            t1 = 1.0;
        } else {
            t0 = (-1.0 - signedDistanceToTrianglePlane) / normalDotVelocity;
            t1 = ( 1.0 - signedDistanceToTrianglePlane) / normalDotVelocity;

            if (t0 > t1) {
                double temp = t1;
                t1 = t0;
                t0 = temp;
            }

            if (t0 > 1.0 || t1 < 0.0) return;

            if (t0 < 0.0) t0 = 0.0;
            if (t1 > 1.0) t1 = 1.0;
        }

        boolean foundCollision = false;
        Vector3f intersectionPoint = null;
        double t = 1.0;

        if (!embeddedInPlane) {
            Vector3f planeIntersectionPoint = Vector3f.add(Vector3f.sub(basePoint, trianglePlane.normal, null), Mth.multiply(velocity, (float)t0), null);

            if (CollisionMath.pointInTriangle(planeIntersectionPoint, p1, p2, p3)) {
                foundCollision = true;
                t = t0;
                intersectionPoint = planeIntersectionPoint;
            }
        }

        if (!foundCollision) {
            float velocityLengthSquared = velocity.lengthSquared();
            float a, b, c;
            Float x1;

            a = velocityLengthSquared;

            b = 2.0f * Vector3f.dot(velocity, Vector3f.sub(basePoint, p1, null));
            c = Vector3f.sub(p1, basePoint, null).lengthSquared() - 1.0f;
            x1 = CollisionMath.getLowestRootUnderThreshold(a, b, c, (float)t);
            if (x1 != null) {
                foundCollision = true;
                t = x1;
                intersectionPoint = p1;
            }

            b = 2.0f * Vector3f.dot(velocity, Vector3f.sub(basePoint, p2, null));
            c = Vector3f.sub(p2, basePoint, null).lengthSquared() - 1.0f;
            x1 = CollisionMath.getLowestRootUnderThreshold(a, b, c, (float)t);
            if (x1 != null) {
                foundCollision = true;
                t = x1;
                intersectionPoint = p2;
            }

            b = 2.0f * Vector3f.dot(velocity, Vector3f.sub(basePoint, p3, null));
            c = Vector3f.sub(p3, basePoint, null).lengthSquared() - 1.0f;
            x1 = CollisionMath.getLowestRootUnderThreshold(a, b, c, (float)t);
            if (x1 != null) {
                foundCollision = true;
                t = x1;
                intersectionPoint = p3;
            }

            Vector3f edge = Vector3f.sub(p2, p1, null);
            Vector3f basePointToVertex = Vector3f.sub(p1, basePoint, null);
            float edgeLengthSquared = edge.lengthSquared();
            float edgeDotVelocity = Vector3f.dot(edge, velocity);
            float edgeDotBaseToVertex = Vector3f.dot(edge, basePointToVertex);

            a = edgeLengthSquared*-velocityLengthSquared + edgeDotVelocity*edgeDotVelocity;
            b = edgeLengthSquared*(2.0f*Vector3f.dot(velocity, basePointToVertex)) - 2.0f*edgeDotVelocity*edgeDotBaseToVertex;
            c = edgeLengthSquared*(1.0f - basePointToVertex.lengthSquared()) + edgeDotBaseToVertex*edgeDotBaseToVertex;
            x1 = CollisionMath.getLowestRootUnderThreshold(a, b, c, (float)t);
            if (x1 != null) {
                float f = (edgeDotVelocity*x1 - edgeDotBaseToVertex) / edgeLengthSquared;
                // TODO: 2024-06-17 does it matter that the conditionals are comparing against float or double?
                if (f >= 0.0f && f <= 1.0f) {
                    foundCollision = true;
                    t = x1;
                    intersectionPoint = Vector3f.add(p1, Mth.multiply(edge, f), null);
                }
            }

            edge = Vector3f.sub(p3, p2, null);
            basePointToVertex = Vector3f.sub(p2, basePoint, null);
            edgeLengthSquared = edge.lengthSquared();
            edgeDotVelocity = Vector3f.dot(edge, velocity);
            edgeDotBaseToVertex = Vector3f.dot(edge, basePointToVertex);

            a = edgeLengthSquared*-velocityLengthSquared + edgeDotVelocity*edgeDotVelocity;
            b = edgeLengthSquared*(2.0f*Vector3f.dot(velocity, basePointToVertex)) - 2.0f*edgeDotVelocity*edgeDotBaseToVertex;
            c = edgeLengthSquared*(1.0f - basePointToVertex.lengthSquared()) + edgeDotBaseToVertex*edgeDotBaseToVertex;
            x1 = CollisionMath.getLowestRootUnderThreshold(a, b, c, (float)t);
            if (x1 != null) {
                float f = (edgeDotVelocity*x1 - edgeDotBaseToVertex) / edgeLengthSquared;
                if (f >= 0.0f && f <= 1.0f) {
                    foundCollision = true;
                    t = x1;
                    intersectionPoint = Vector3f.add(p2, Mth.multiply(edge, f), null);
                }
            }

            edge = Vector3f.sub(p1, p3, null);
            basePointToVertex = Vector3f.sub(p3, basePoint, null);
            edgeLengthSquared = edge.lengthSquared();
            edgeDotVelocity = Vector3f.dot(edge, velocity);
            edgeDotBaseToVertex = Vector3f.dot(edge, basePointToVertex);

            a = edgeLengthSquared*-velocityLengthSquared + edgeDotVelocity*edgeDotVelocity;
            b = edgeLengthSquared*(2.0f*Vector3f.dot(velocity, basePointToVertex)) - 2.0f*edgeDotVelocity*edgeDotBaseToVertex;
            c = edgeLengthSquared*(1.0f - basePointToVertex.lengthSquared()) + edgeDotBaseToVertex*edgeDotBaseToVertex;
            x1 = CollisionMath.getLowestRootUnderThreshold(a, b, c, (float)t);
            if (x1 != null) {
                float f = (edgeDotVelocity*x1 - edgeDotBaseToVertex) / edgeLengthSquared;
                if (f >= 0.0f && f <= 1.0f) {
                    foundCollision = true;
                    t = x1;
                    intersectionPoint = Vector3f.add(p3, Mth.multiply(edge, f), null);
                }
            }
        }

        if (foundCollision) {
            // TODO: 2024-06-18 Reconsider where the cast should be done
            float intersectionDistance = (float)t * velocity.length();

            if (!collisionInfo.foundCollision || intersectionDistance < collisionInfo.intersectionDistance) {
                collisionInfo.foundCollision = true;
                collisionInfo.intersectionDistance = intersectionDistance;
                collisionInfo.intersectionPoint = intersectionPoint;
            }
        }
    }

    private static class CollisionInfo {
        private boolean foundCollision;
        private float intersectionDistance;
        private Vector3f intersectionPoint;
    }

}
