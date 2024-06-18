package com.james.newCollisionsStuff;

import com.james.math.Mth;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class NewCollisionHandler {

    // TODO: 2024-01-16 Consider changing this to check to accept your custom triangle class instead of the three points
    // Note: this method is implemented from Fauerby's report:
    // http://www.peroxide.dk/papers/collision/collision.pdf
    public static void checkTriangle(CollisionPacket collisionPacket, Vector3f p1, Vector3f p2, Vector3f p3) {
        Plane trianglePlane = new Plane(p1, p2, p3);

        if (trianglePlane.isFrontFacingTo(collisionPacket.eNormalizedVelocity)) {
            float t0, t1;
            boolean embeddedInPlane = false;

            float signedDistanceToTrianglePlane = trianglePlane.signedDistanceTo(collisionPacket.eBasePoint);

            float normalDotVelocity = Vector3f.dot(trianglePlane.normal, collisionPacket.eVelocity);

            if (normalDotVelocity == 0.0f) {
                if (Math.abs(signedDistanceToTrianglePlane) >= 1.0f) {
                    return;
                } else {
                    embeddedInPlane = true;
                    t0 = 0.0f;
                    t1 = 1.0f;
                }
            } else {
                t0 = (-1 - signedDistanceToTrianglePlane) / normalDotVelocity;
                t1 = (1 - signedDistanceToTrianglePlane) / normalDotVelocity;

                if (t0 > t1) {
                    float temp = t1;
                    t1 = t0;
                    t0 = temp;
                }

                if (t0 > 1.0 || t1 < 0.0) {
                    return;
                }

                if (t0 < 0.0) t0 = 0.0f;
                if (t1 < 0.0) t1 = 0.0f;
                if (t0 > 1.0) t0 = 1.0f;
                if (t1 > 1.0) t1 = 1.0f;
            }

            Vector3f collisionPoint = null;
            boolean foundCollision = false;
            float t = 1.0f;

            if (!embeddedInPlane) {
                Vector3f leftSide = Vector3f.sub(collisionPacket.eBasePoint, trianglePlane.normal, null);
                Vector3f rightSide = Mth.multiply(collisionPacket.eVelocity, t0);
                Vector3f planeIntersectionPoint = Vector3f.add(leftSide, rightSide, null);

                if (Mth.pointInTriangle(p1, p2, p3, planeIntersectionPoint)) {
                    foundCollision = true;
                    t = t0;
                    collisionPoint = planeIntersectionPoint;
                }
            }

            if (!foundCollision) {
                Vector3f velocity = collisionPacket.eVelocity;
                Vector3f base = collisionPacket.eBasePoint;

                float velocitySquaredLength = velocity.lengthSquared();
                float a = velocitySquaredLength;
                float b, c;
                Float newT;

                b = 2.0f * Vector3f.dot(velocity, Vector3f.sub(base, p1, null));
                c = Vector3f.sub(p1, base, null).lengthSquared() - 1.0f;

                newT = Mth.getLowestRootUnderThreshold(a, b, c, t);
                if (newT != null) {
                    t = newT;
                    foundCollision = true;
                    collisionPoint = p1;
                }

                b = 2.0f * Vector3f.dot(velocity, Vector3f.sub(base, p2, null));
                c = Vector3f.sub(p2, base, null).lengthSquared() - 1.0f;
                newT = Mth.getLowestRootUnderThreshold(a, b, c, t);
                if (newT != null) {
                    t = newT;
                    foundCollision = true;
                    collisionPoint = p2;
                }

                b = 2.0f * Vector3f.dot(velocity, Vector3f.sub(base, p3, null));
                c = Vector3f.sub(p3, base, null).lengthSquared() - 1.0f;
                newT = Mth.getLowestRootUnderThreshold(a, b, c, t);
                if (newT != null) {
                    t = newT;
                    foundCollision = true;
                    collisionPoint = p3;
                }

                Vector3f edge = Vector3f.sub(p2, p1, null);
                Vector3f baseToVertex = Vector3f.sub(p1, base, null);
                float edgeSquaredLength = edge.lengthSquared();
                float edgeDotVelocity = Vector3f.dot(edge, velocity);
                float edgeDotBaseToVertex = Vector3f.dot(edge, baseToVertex);

                a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
                b = edgeSquaredLength * (2 * Vector3f.dot(velocity, baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
                c = edgeSquaredLength * (1 - baseToVertex.lengthSquared()) + edgeDotBaseToVertex * edgeDotBaseToVertex;

                newT = Mth.getLowestRootUnderThreshold(a, b, c, t);
                if (newT != null) {
                    float f = (edgeDotVelocity * newT - edgeDotBaseToVertex) / edgeSquaredLength;
                    if (f >= 0 && f <= 1) {
                        t = newT;
                        foundCollision = true;
                        collisionPoint = Vector3f.add(p1, Mth.multiply(edge, f), null);
                    }
                }

                edge = Vector3f.sub(p3, p2, null);
                baseToVertex = Vector3f.sub(p2, base, null);
                edgeSquaredLength = edge.lengthSquared();
                edgeDotVelocity = Vector3f.dot(edge, velocity);
                edgeDotBaseToVertex = Vector3f.dot(edge, baseToVertex);

                a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
                b = edgeSquaredLength * (2 * Vector3f.dot(velocity, baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
                c = edgeSquaredLength * (1 - baseToVertex.lengthSquared()) + edgeDotBaseToVertex * edgeDotBaseToVertex;

                newT = Mth.getLowestRootUnderThreshold(a, b, c, t);
                if (newT != null) {
                    float f = (edgeDotVelocity * newT - edgeDotBaseToVertex) / edgeSquaredLength;
                    if (f >= 0.0 && f <= 1.0) {
                        t = newT;
                        foundCollision = true;
                        collisionPoint = Vector3f.add(p2, Mth.multiply(edge, f), null);
                    }
                }

                edge = Vector3f.sub(p1, p3, null);
                baseToVertex = Vector3f.sub(p3, base, null);
                edgeSquaredLength = edge.lengthSquared();
                edgeDotVelocity = Vector3f.dot(edge, velocity);
                edgeDotBaseToVertex = Vector3f.dot(edge, baseToVertex);

                a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
                b = edgeSquaredLength * (2 * Vector3f.dot(velocity, baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
                c = edgeSquaredLength * (1 - baseToVertex.lengthSquared()) + edgeDotBaseToVertex * edgeDotBaseToVertex;

                newT = Mth.getLowestRootUnderThreshold(a, b, c, t);
                if (newT != null) {
                    float f = (edgeDotVelocity * newT - edgeDotBaseToVertex) / edgeSquaredLength;
                    if (f >= 0.0 && f <= 1.0) {
                        t = newT;
                        foundCollision = true;
                        collisionPoint = Vector3f.add(p3, Mth.multiply(edge, f), null);
                    }
                }
            }

            if (foundCollision) {
                float distanceToCollision = t * collisionPacket.eVelocity.length();

                if (!collisionPacket.foundCollision || distanceToCollision < collisionPacket.nearestDistance) {
                    collisionPacket.nearestDistance = distanceToCollision;
                    collisionPacket.intersectionPoint = collisionPoint;
                    collisionPacket.foundCollision = true;
                }
            }
        }
    }

    private static int collisionRecursionDepth; //
    private static final CollisionPacket collisionPacket = new CollisionPacket(); //

    // Note: this method is implemented from Fauerby's report:
    // http://www.peroxide.dk/papers/collision/collision.pdf
    public static Vector3f collideAndSlide(Vector3f velocity, Vector3f gravity, Vector3f position, Vector3f ellipsoidRadius) {
        collisionPacket.ellipsoidRadius = ellipsoidRadius; //

        collisionPacket.r3Position = position;
        collisionPacket.r3Velocity = velocity;

        Vector3f eSpacePosition = Mth.divideVectors(collisionPacket.r3Position, collisionPacket.ellipsoidRadius);
        Vector3f eSpaceVelocity = Mth.divideVectors(collisionPacket.r3Velocity, collisionPacket.ellipsoidRadius);

        collisionRecursionDepth = 0;

        Vector3f finalPosition = collideWithWorld(eSpacePosition, eSpaceVelocity);

        // TODO: 2024-01-22 Is the following line necessary? r3Position doesn't seem to be used after...
        collisionPacket.r3Position = Mth.multiplyVectors(finalPosition, collisionPacket.ellipsoidRadius);
        collisionPacket.r3Velocity = gravity;

        eSpaceVelocity = Mth.divideVectors(gravity, collisionPacket.ellipsoidRadius);

        collisionRecursionDepth = 0;

        finalPosition = collideWithWorld(finalPosition, eSpaceVelocity);

        finalPosition = Mth.multiplyVectors(finalPosition, collisionPacket.ellipsoidRadius);

        return finalPosition; //
    }

    private static final float unitsPerMeter = 100.0f; //

    // TODO: 2024-01-22 Be wary of copying that happens in C++ which doesn't happen in Java
    // Note: this method is implemented from Fauerby's report:
    // http://www.peroxide.dk/papers/collision/collision.pdf
    private static Vector3f collideWithWorld(Vector3f position, Vector3f velocity) {
        float unitScale = unitsPerMeter / 100.0f;
        float veryCloseDistance = 0.005f * unitScale;

        if (collisionRecursionDepth > 5)
            return position;

        collisionPacket.eVelocity = velocity;
        collisionPacket.eNormalizedVelocity = velocity.normalise(null);
        collisionPacket.eBasePoint = position;
        collisionPacket.foundCollision = false;

        // WORLD CHECK COLLISION CODE GOES HERE!
        collisionPacket.foundCollision = new Random().nextBoolean();

        if (!collisionPacket.foundCollision) {
            return Vector3f.add(position, velocity, null);
        }

        Vector3f destinationPoint = Vector3f.add(position, velocity, null);
        Vector3f newBasePoint = position;

        if (collisionPacket.nearestDistance >= veryCloseDistance) {
            Vector3f V = new Vector3f(velocity);
            Mth.setLength(V, collisionPacket.nearestDistance - veryCloseDistance);
            newBasePoint = Vector3f.add(collisionPacket.eBasePoint, V, null);
            V.normalise(V);
            Vector3f.sub(collisionPacket.intersectionPoint, Mth.multiply(V, veryCloseDistance), collisionPacket.intersectionPoint);
        }

        Vector3f slidePlaneOrigin = new Vector3f(collisionPacket.intersectionPoint);
        Vector3f slidePlaneNormal = Vector3f.sub(newBasePoint, collisionPacket.intersectionPoint, null);
        slidePlaneNormal.normalise(slidePlaneNormal);
        Plane slidingPlane = new Plane(slidePlaneOrigin, slidePlaneNormal);

        Vector3f newDestinationPoint = Vector3f.sub(destinationPoint, Mth.multiply(slidePlaneNormal, slidingPlane.signedDistanceTo(destinationPoint)), null);

        Vector3f newVelocityVector = Vector3f.sub(newDestinationPoint, collisionPacket.intersectionPoint, null);

        if (newVelocityVector.length() < veryCloseDistance) {
            return newBasePoint;
        }

        collisionRecursionDepth++;
        return collideWithWorld(newBasePoint, newVelocityVector);
    }

}

