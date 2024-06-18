package com.james.world;

import org.lwjgl.util.vector.Vector3f;

/**
 * An extension of PhysicalObject that allows for velocity and acceleration values, which get applied in the
 * overloaded update() method every game tick.
 */
public class MovableObject extends PhysicalObject {

    private final Level level;
    private final Vector3f velocity = new Vector3f(0, 0, 0);
    private final Vector3f acceleration = new Vector3f(0, 0, 0);

    public MovableObject(Level level, Vector3f position, Vector3f rotation, float scale) {
        super(position, rotation, scale);
        this.level = level;
    }

    public void setVelocity(float x, float y, float z) {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }

    public void setAcceleration(float x, float y, float z) {
        this.acceleration.x = x;
        this.acceleration.y = y;
        this.acceleration.z = z;
    }

    public Vector3f getVelocity() { return velocity; }

    public Vector3f getAcceleration() { return acceleration; }

    @Override
    public boolean update() {
        this.velocity.x += this.acceleration.x * level.secondsPerGameTick;
        this.velocity.y += this.acceleration.y * level.secondsPerGameTick;
        this.velocity.z += this.acceleration.z * level.secondsPerGameTick;

        this.position.x += this.velocity.x * level.secondsPerGameTick;
        this.position.y += this.velocity.y * level.secondsPerGameTick;
        this.position.z += this.velocity.z * level.secondsPerGameTick;

        return super.update();
    }

}
