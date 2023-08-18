package com.james.renderEngine.particles.dataTypes;

import java.util.Random;

public class RandomFloat implements FloatValue {

    private final float a;
    private final float b;

    private final Random r = new Random();

    public RandomFloat(float a, float b) {
        this.a = a;
        this.b = b;
    }

    public float get() {
        float normalizedRandomValue = r.nextFloat();
        float gapBetweenValues = a - b;
        return normalizedRandomValue * gapBetweenValues + a;
    }

}
