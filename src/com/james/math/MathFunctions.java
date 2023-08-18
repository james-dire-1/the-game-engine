package com.james.math;

import java.util.function.Function;

public class MathFunctions {

    public static final Function<Float, Float> Linear = MathFunctions::linear;

    public static float linear(float x) {
        return x;
    }

    public static float quadratic(float x) {
        return x * x;
    }

    public static float cubic(float x) {
        return x * x * x;
    }

    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    public static float sine(float x) {
        return (float) Math.sin(Math.PI/2 * x);
    }

}
