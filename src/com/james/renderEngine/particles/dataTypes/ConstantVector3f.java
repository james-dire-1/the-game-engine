package com.james.renderEngine.particles.dataTypes;

import org.lwjgl.util.vector.Vector3f;

public class ConstantVector3f implements Vector3fValue {

    private final Vector3f value;

    public ConstantVector3f(float x, float y, float z) {
        this.value = new Vector3f(x, y, z);
    }

    @Override
    public Vector3f get() {
        return value;
    }

}
