package com.james.world;

import org.lwjgl.util.vector.Vector3f;

/**
 * Class representing a single triangle in a mesh that is meant to function as a wall. It contains an
 * ExtendingMode (used for calculations in the collision code), and a lowest and highest coordinate (again, also
 * used in the collision code, and its interpretation of either being x-coordinates or z-coordinates is given
 * by ExtendingMode).
 * Note: this class is used by the server as well as by the client, since both make use of the same fields.
 * However, this is not the same case with ObjectHitboxes, which do have a client side equivalent
 * (CachedObjectHitbox). So just keep that in mind.
 * @see ObjectHitbox
 */
public class WallTriangle extends Triangle {

    public final ExtendingMode mode;
    public final float lowestCoordinate;
    public final float highestCoordinate;

    /**
     * Constructor to be used on the server side that sets the points, and determines the ExtendingMode mode,
     * lowestCoordinate, and highestCoordinate based on these points.
     * @param points the three points that make up the wall triangle, each point being a Vector3f
     */
    public WallTriangle(Vector3f[] points) {
        super(points);

        float facingAngleOfWall = (float) Math.toDegrees(Math.atan2(-unitVector.z, unitVector.x));

        if ((facingAngleOfWall > 45 && facingAngleOfWall < 135) || (facingAngleOfWall > -135 && facingAngleOfWall < -45)) {
            this.mode = ExtendingMode.Vertical;
            this.lowestCoordinate = Math.min(Math.min(points[0].x, points[1].x), points[2].x);
            this.highestCoordinate = Math.max(Math.max(points[0].x, points[1].x), points[2].x);
        } else {
            this.mode = ExtendingMode.Horizontal;
            this.lowestCoordinate = Math.min(Math.min(points[0].z, points[1].z), points[2].z);
            this.highestCoordinate = Math.max(Math.max(points[0].z, points[1].z), points[2].z);
        }
    }

    /**
     * Constructor to be used client-side.
     */
    public WallTriangle(WallTriangle wallTriangle) {
        // TODO: 2024-01-14 shouldn't we be calling the super constructor for client side in here??
        super(wallTriangle.points);
        this.mode = wallTriangle.mode;
        this.lowestCoordinate = wallTriangle.lowestCoordinate;
        this.highestCoordinate = wallTriangle.highestCoordinate;
    }

    public enum ExtendingMode {
        Vertical, Horizontal
    }

}
