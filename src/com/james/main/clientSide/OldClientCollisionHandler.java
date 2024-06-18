package com.james.main.clientSide;

import com.james.collisions.OldCollisionMath;
import com.james.main.OldPlayerHitbox;
import com.james.math.Mth;
import com.james.tools.Time;
import com.james.world.OldCollisionHandler;
import com.james.world.WallTriangle;
import org.lwjgl.util.vector.Vector3f;

/**
 * Client side version of CollisionHandler.
 * @see OldCollisionHandler
 */
public class OldClientCollisionHandler {

    /**
     * Gets called once per frame.
     */
    // TODO: 2024-01-15 find a way to make this get called in fixed time intervals rather than tying it to
    // TODO: 2024-01-15 framerate, since this is technically a simulation related thing
    public static void update() {
        for (CachedObjectHitbox hitbox : ClientPacketReceiveActions.cachedLocalObjectHitboxes) {
            collisionBetweenObjects(ClientPacketReceiveActions.oldPlayerHitbox, hitbox);
        }

        for (WallTriangle triangle : ClientPacketReceiveActions.cachedLocalWallTriangles) {
            // TODO: 2024-06-02 move this first line out of the for loop. Why is it in here??
            Vector3f playerPosition = ClientPacketReceiveActions.player.getPosition();

            if (triangle.mode == WallTriangle.ExtendingMode.Vertical) {
                if (playerPosition.x > triangle.lowestCoordinate && playerPosition.x < triangle.highestCoordinate) {
                    OldCollisionMath.collisionBetweenObjectAndWallTriangle(ClientPacketReceiveActions.oldPlayerHitbox, triangle);
                }
            } else if (triangle.mode == WallTriangle.ExtendingMode.Horizontal) {
                if (playerPosition.z > triangle.lowestCoordinate && playerPosition.z < triangle.highestCoordinate) {
                    OldCollisionMath.collisionBetweenObjectAndWallTriangle(ClientPacketReceiveActions.oldPlayerHitbox, triangle);
                }
            }
        }
    }

    private static void collisionBetweenObjects(OldPlayerHitbox playerHitbox, CachedObjectHitbox otherHitbox) {
        float distanceApartBetweenCenterPoints = Mth.distance(playerHitbox.player.getPosition(), otherHitbox.object.getPosition());
        float sumOfRadii = playerHitbox.radius + otherHitbox.radius;

        // a collision has occurred
        if (distanceApartBetweenCenterPoints < sumOfRadii) {
            float distanceToBeTravelled = sumOfRadii - distanceApartBetweenCenterPoints;
            respondToObjectCollision(playerHitbox, otherHitbox, distanceToBeTravelled);
        }
    }

    // TODO: 2024-06-06 consider moving this into the CollisionMath class, as this matches with the server side code
    private static void respondToObjectCollision(OldPlayerHitbox responder, CachedObjectHitbox other, float distanceToBeTravelled) {
        Vector3f directionToTravel = Vector3f.sub(responder.player.getPosition(), other.object.getPosition(), null);
        directionToTravel.normalise(directionToTravel);

        float speed = distanceToBeTravelled / ClientPacketReceiveActions.secondsTillEndCollisionForPlayer;

        Vector3f movementThisFrame = Mth.multiply(directionToTravel, speed * Time.getDeltaTime());

        responder.player.setPosition(movementThisFrame.x, movementThisFrame.y, movementThisFrame.z);
    }

}
