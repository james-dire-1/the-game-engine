package com.james.main.clientSide;

import com.james.collisions.CollisionMath;
import com.james.main.PlayerHitbox;
import com.james.math.Mth;
import com.james.tools.Time;
import com.james.world.WallTriangle;
import org.lwjgl.util.vector.Vector3f;

/**
 * Client side version of CollisionHandler.
 * @see com.james.world.CollisionHandler
 */
public class ClientCollisionHandler {

    /**
     * Gets called once per frame.
     */
    // TODO: 2024-01-15 find a way to make this get called in fixed time intervals rather than tying it to
    // TODO: 2024-01-15 framerate, since this is technically a simulation related thing
    public static void update() {
        for (CachedObjectHitbox hitbox : ClientPacketReceiveActions.cachedLocalObjectHitboxes) {
            collisionBetweenObjects(ClientPacketReceiveActions.playerHitbox, hitbox);
        }

        for (WallTriangle triangle : ClientPacketReceiveActions.cachedLocalWallTriangles) {
            Vector3f playerPosition = ClientPacketReceiveActions.player.getPosition();

            if (triangle.mode == WallTriangle.ExtendingMode.Vertical) {
                if (playerPosition.x > triangle.lowestCoordinate && playerPosition.x < triangle.highestCoordinate) {
                    CollisionMath.collisionBetweenObjectAndWallTriangle(ClientPacketReceiveActions.playerHitbox, triangle);
                }
            } else if (triangle.mode == WallTriangle.ExtendingMode.Horizontal) {
                if (playerPosition.z > triangle.lowestCoordinate && playerPosition.z < triangle.highestCoordinate) {
                    CollisionMath.collisionBetweenObjectAndWallTriangle(ClientPacketReceiveActions.playerHitbox, triangle);
                }
            }
        }
    }

    private static void collisionBetweenObjects(PlayerHitbox playerHitbox, CachedObjectHitbox otherHitbox) {
        float distanceApartBetweenCenterPoints = Mth.distance(playerHitbox.player.getPosition(), otherHitbox.object.getPosition());
        float sumOfRadii = playerHitbox.radius + otherHitbox.radius;

        // a collision has occurred
        if (distanceApartBetweenCenterPoints < sumOfRadii) {
            float distanceToBeTravelled = sumOfRadii - distanceApartBetweenCenterPoints;
            respondToObjectCollision(playerHitbox, otherHitbox, distanceToBeTravelled);
        }
    }

    private static void respondToObjectCollision(PlayerHitbox responder, CachedObjectHitbox other, float distanceToBeTravelled) {
        Vector3f directionToTravel = Vector3f.sub(responder.player.getPosition(), other.object.getPosition(), null);
        directionToTravel.normalise(directionToTravel);

        float speed = distanceToBeTravelled / ClientPacketReceiveActions.secondsTillEndCollisionForPlayer;

        Vector3f movementThisFrame = Mth.multiply(directionToTravel, speed * Time.getDeltaTime());

        responder.player.setPosition(movementThisFrame.x, movementThisFrame.y, movementThisFrame.z);
    }

}
