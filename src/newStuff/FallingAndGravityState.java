package newStuff;

import com.james.common.simulation.LevelProperties;
import com.james.common.simulation.collisionEngine.math.containers.CollisionDetails;
import com.james.serverSide.simulation.objects.MovableObject;
import org.lwjgl.util.vector.Vector3f;

public class FallingAndGravityState {

    private static final float EPSILON = 0.005f;

    private final FallingAndGravityProperties fgProperties;
    private final Vector3f velocityDueToGravity = new Vector3f();
    public Vector3f getVelocityDueToGravity() { return velocityDueToGravity; }

    private State state = State.FALLING;
    private int ticksElapsed;

    public FallingAndGravityState(FallingAndGravityProperties fgProperties) {
        this.fgProperties = fgProperties;
    }

    public void update(MovableObject movableObject, LevelProperties levelProperties) {
        CollisionDetails collisionDetails = movableObject.getCollisionDetails();
        Vector3f velocity = movableObject.getVelocity();
        boolean moving = Math.abs(velocity.x) > EPSILON || Math.abs(velocity.y) > EPSILON || Math.abs(velocity.z) > EPSILON;
        float targetGravityVelocity = fgProperties.getTargetGravityVelocityForInclinationAndMoving(collisionDetails.inclinationAngle, moving);

        if (state == State.FALLING) {
            ticksElapsed++;

            if (collisionDetails.onGround) {
                if (ticksElapsed > 5) {
                    state = State.ON_GROUND_RECOVERING;
                    ticksElapsed = 0;
                } else {
                    state = State.ON_GROUND_FIRMLY;
                }
            } else {
                velocityDueToGravity.y += levelProperties.gravityAcceleration;

                if (velocityDueToGravity.y < levelProperties.terminalVelocity) {
                    velocityDueToGravity.y = levelProperties.terminalVelocity;
                }
            }
        }

        else if (state == State.ON_GROUND_RECOVERING) {
            ticksElapsed++;

            if (!collisionDetails.onGround) {
                state = State.FALLING;
                ticksElapsed = 0;
            } else if (ticksElapsed * levelProperties.secondsPerGameTick >= fgProperties.maxSecondsNeededToRecoverFromFall) {
                state = State.ON_GROUND_FIRMLY;
            } else {
                if (velocityDueToGravity.y >= targetGravityVelocity - EPSILON) {
                    velocityDueToGravity.y = targetGravityVelocity;
                    state = State.ON_GROUND_FIRMLY;
                } else {
                    velocityDueToGravity.y -= levelProperties.gravityAcceleration;

                    if (velocityDueToGravity.y > targetGravityVelocity) {
                        velocityDueToGravity.y = targetGravityVelocity;
                    }
                }
            }
        }

        else if (state == State.ON_GROUND_FIRMLY) {
            velocityDueToGravity.y = targetGravityVelocity;

            if (!collisionDetails.onGround) {
                state = State.FALLING;
                ticksElapsed = 0;
            }
        }
    }

    private enum State {
        FALLING,
        ON_GROUND_RECOVERING,
        ON_GROUND_FIRMLY
    }

}
