package com.james.renderEngine.particles;

import com.james.math.Mth;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

public class Particle {

    public Vector3f position;
    public Vector3f velocity;
    public float rotation;
    public float scale;
    public float gravityMultiplier;

    private final float lifetime;

    protected final float instantiationTime = Time.getCurrentTime();

    public Particle(Vector3f position, Vector3f velocity, float rotation, float scale, float gravityMultiplier, float lifetime) {
        this.position = position;
        this.velocity = velocity;
        this.rotation = rotation;
        this.scale = scale;
        this.gravityMultiplier = gravityMultiplier;
        this.lifetime = lifetime;

        ParticleHandler.particles.add(this);
    }

    /**
     * Updates the particle's attributes for this frame.
     * @return whether the particle should be removed from the scene, based on its lifetime value
     */
    public boolean update() {
        velocity.y += ParticleHandler.GRAVITY * gravityMultiplier;
        Vector3f movementThisFrame = Mth.multiply(velocity, Time.getDeltaTime());
        Vector3f.add(position, movementThisFrame, position);

        return Time.getCurrentTime() - instantiationTime > lifetime;
    }

}
