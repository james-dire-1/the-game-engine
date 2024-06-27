package com.james.renderEngine.particles;

import com.james.renderEngine.particles.math.EasingMath;
import com.james.renderEngine.particles.math.MathFunctions;
import com.james.renderEngine.particles.dataTypes.FloatType;
import com.james.renderEngine.particles.dataTypes.KeyframeType;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.function.Function;

/**
 * Represents a single particle that can change its attributes (position, velocity, rotation, scale,
 * gravity multiplier) at specific timestamps to create more interesting particle effects that aren't
 * seen with the generic Particle class. To achieve these effects, the complex particles uses keyframe
 * data from an instance of the ComplexParticleSettings class, which can be reused for as many instances
 * of this class as you desire.
 * @see Particle
 * @see ComplexParticleSettings
 */
public class ComplexParticle extends Particle {

    private final Map<FloatType, KeyframeType> keyframeTypes;
    private final Map<FloatType, SortedMap<Float, Float>> allKeyframeData;

    private static final Random r = new Random();

    /**
     * Creates a new complex particle with specified lifetime and settings.
     *
     * @implNote It is possible that the user did not specify a value for any given attribute for the
     * timestamp 0 seconds, as they might want the default value for that attribute (usually 0 or 1 for float
     * attributes and (0, 0, 0) for vector attributes) to be used in the easing calculations for the timestamp
     * 0. However, the getFinalValueForFloatType() method below requires a timestamp at 0 seconds for it to
     * work properly. Therefore, the ComplexParticleSettings instance's finalizeParticleSettings() method
     * is called here to add in any missing keyframes for the timestamp of 0 seconds, for all attributes
     * that don't already have such a keyframe.
     *
     */
    public ComplexParticle(float lifetime, ComplexParticleSettings settings) {
        super(new Vector3f(), new Vector3f(), 0, 1, 1, lifetime);

        settings.finalizeParticleSettings(lifetime);

        this.keyframeTypes = settings.extractKeyframeTypes();
        this.allKeyframeData = settings.extractAllKeyframeDataAndApplyValues();
    }

    /**
     * Override update() method. This override is only responsible for changing the attributes of the particle.
     * Then it transfers responsibility to the super class' update() method.
     */
    @Override
    public boolean update() {
        float elapsedSeconds = Time.getCurrentTime() - instantiationTime;

        // calculate what to put in the rotation variable
        float rotation = getFinalValueForFloatType(elapsedSeconds, FloatType.Rotation, MathFunctions.Linear);
        KeyframeType keyframeTypeForRotation = keyframeTypes.get(FloatType.Rotation);

        if (keyframeTypeForRotation == KeyframeType.Target) {
            this.rotation = rotation;
        } else if (keyframeTypeForRotation == KeyframeType.Change) {
            this.rotation += rotation;
        }

        // calculate what to put in the scale variable
        float scale = getFinalValueForFloatType(elapsedSeconds, FloatType.Scale, MathFunctions.Linear);
        KeyframeType keyframeTypeForScale = keyframeTypes.get(FloatType.Scale);

        if (keyframeTypeForScale == KeyframeType.Target) {
            this.scale = scale;
        } else if (keyframeTypeForScale == KeyframeType.Change) {
            this.scale += scale;
        }

        // calculate what to put in the gravity multiplier variable
        float gravityMultiplier = getFinalValueForFloatType(elapsedSeconds, FloatType.GravityMultiplier, MathFunctions.Linear);
        KeyframeType keyframeTypeForGravityMultiplier = keyframeTypes.get(FloatType.GravityMultiplier);

        if (keyframeTypeForGravityMultiplier == KeyframeType.Target) {
            this.gravityMultiplier = gravityMultiplier;
        } else if (keyframeTypeForGravityMultiplier == KeyframeType.Change) {
            this.gravityMultiplier += gravityMultiplier;
        }

        return super.update();
    }

    /**
     *
     * @param elapsedSeconds
     * @param type
     * @param easingFunction
     * @return
     */
    private float getFinalValueForFloatType(float elapsedSeconds, FloatType type, Function<Float, Float> easingFunction) {
        SortedMap<Float, Float> keyframeData = allKeyframeData.get(type);

        Float[] timestamps = keyframeData.keySet().toArray(new Float[0]);

        int timestampOfLowerBoundKeyframe = 0;
        int timestampOfUpperBoundKeyframe = 0;
        Float lowerBoundValue = 0f;
        Float upperBoundValue = 0f;

        for (int i = 0; i < timestamps.length; i++) {
            if (i + 1 == timestamps.length) {
                lowerBoundValue = keyframeData.get(timestamps[i]);
                return lowerBoundValue;
            }

            if (elapsedSeconds >= timestamps[i] && elapsedSeconds < timestamps[i + 1]) {
                timestampOfLowerBoundKeyframe = i;
                timestampOfUpperBoundKeyframe = i + 1;

                lowerBoundValue = keyframeData.get(timestamps[timestampOfLowerBoundKeyframe]);
                upperBoundValue = keyframeData.get(timestamps[timestampOfUpperBoundKeyframe]);

                break;
            }
        }

        float normalizedProgress = EasingMath.getNormalizedProgress(timestamps[timestampOfLowerBoundKeyframe], timestamps[timestampOfUpperBoundKeyframe], elapsedSeconds);
        float valueAtCurrentTimeBeforeEasing = EasingMath.linearInterpolation(lowerBoundValue, upperBoundValue, normalizedProgress);

        if (easingFunction == MathFunctions.Linear) {
            return valueAtCurrentTimeBeforeEasing;
        }

        return EasingMath.easeBetweenValuesByFunction(lowerBoundValue, upperBoundValue, valueAtCurrentTimeBeforeEasing, easingFunction);
    }

}
