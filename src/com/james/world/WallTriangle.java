package com.james.world;

import org.lwjgl.util.vector.Vector3f;

public class WallTriangle extends Triangle {

    public final ExtendingMode mode;
    public final float lowestCoordinate;
    public final float highestCoordinate;

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
        super(wallTriangle.points);
        this.mode = wallTriangle.mode;
        this.lowestCoordinate = wallTriangle.lowestCoordinate;
        this.highestCoordinate = wallTriangle.highestCoordinate;
    }

    public enum ExtendingMode {
        Vertical, Horizontal
    }

}
