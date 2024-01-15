package com.james.world;

import com.james.collisions.CollisionMath;
import com.james.math.Mth;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that handles collisions on the server side. It handles object-object collisions, as well as
 * object-mesh collisions. This class is also where the ObjectHitboxes and WallTriangles for MeshHitboxes
 * are stored.
 */
public class CollisionHandler {

    private final Level level;

    public final List<ObjectHitbox> hitboxes = new ArrayList<>();
    public final List<WallTriangle> walls = new ArrayList<>();

    public CollisionHandler(Level level) {
        this.level = level;
    }

    /**
     * Gets called once per game tick.
     */
    public void update() {
        for (int i = 0; i < hitboxes.size(); i++) {
            ObjectHitbox theTester = hitboxes.get(i);

            for (int j = i+1; j < hitboxes.size(); j++) {
                ObjectHitbox theTestee = hitboxes.get(j);

                collisionBetweenObjects(theTester, theTestee);
            }
        }

        for (ObjectHitbox objectHitbox : hitboxes) {
            for (WallTriangle triangle : walls) {

                Vector3f objectPosition = objectHitbox.object.getPosition();

                if (triangle.mode == WallTriangle.ExtendingMode.Vertical) {
                    if (objectPosition.x > triangle.lowestCoordinate && objectPosition.x < triangle.highestCoordinate) {
                        CollisionMath.collisionBetweenObjectAndWallTriangle(objectHitbox, triangle);
                    }
                } else if (triangle.mode == WallTriangle.ExtendingMode.Horizontal) {
                    if (objectPosition.z > triangle.lowestCoordinate && objectPosition.z < triangle.highestCoordinate) {
                        CollisionMath.collisionBetweenObjectAndWallTriangle(objectHitbox, triangle);
                    }
                }

            }
        }

    }

    private void collisionBetweenObjects(ObjectHitbox a, ObjectHitbox b) {
        float distanceApartBetweenCenterPoints = Mth.distance(a.object.getPosition(), b.object.getPosition());
        float sumOfRadii = a.radius + b.radius;

        // a collision has occurred
        if (distanceApartBetweenCenterPoints < sumOfRadii) {
            float distanceToBeTravelled = sumOfRadii - distanceApartBetweenCenterPoints;

            if (a.reactWhenPushed) {
                respondToObjectCollision(a, b, distanceToBeTravelled);
            }
            if (b.reactWhenPushed) {
                respondToObjectCollision(b, a, distanceToBeTravelled);
            }
        }
    }

    private void respondToObjectCollision(ObjectHitbox responder, ObjectHitbox other, float distanceToBeTravelled) {
        Vector3f directionToTravel = Vector3f.sub(responder.object.getPosition(), other.object.getPosition(), null);
        directionToTravel.normalise(directionToTravel);

        float speed = distanceToBeTravelled / responder.secondsTillEndCollision;

        Vector3f movementThisFrame = Mth.multiply(directionToTravel, speed * level.secondsPerGameTick);

        responder.object.setPosition(movementThisFrame.x, movementThisFrame.y, movementThisFrame.z);
    }

}
