package com.james.renderEngine.particles.dataTypes;

public class ConstantValue implements FloatValue {

    private final float value;

    public ConstantValue(float value) {
        this.value = value;
    }

    @Override
    public float get() {
        return value;
    }

}
