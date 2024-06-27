package com.james.renderEngine.particles.math;

import java.util.function.Function;

public class EasingMath {

    public static float getNormalizedProgress(float startValue, float endValue, float progress) {
        float distanceBetweenExtremities = endValue - startValue;
        float progressSinceStart = progress - startValue;
        return progressSinceStart / distanceBetweenExtremities;
    }

    public static float linearInterpolation(float a, float b, float progress) {
        float distance = b - a;
        return a + distance * progress;
    }

    public static float easeBetweenValuesByFunction(float startValue, float endValue, float currentValue, Function<Float, Float> mathFunction) {
        float normalizedProgress = getNormalizedProgress(startValue, endValue, currentValue);

        float outputOfFunction = mathFunction.apply(normalizedProgress);
        float distanceBetweenExtremities = endValue - startValue;
        float realProgressSinceStart = outputOfFunction * distanceBetweenExtremities;

        return startValue + realProgressSinceStart;
    }

}
