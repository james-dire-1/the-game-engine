package com.james.renderEngine.particles.dataTypes;

import org.lwjgl.util.vector.Vector3f;

public class RandomVector3f implements Vector3fValue {

    private final RandomFloat randomX;
    private final RandomFloat randomY;
    private final RandomFloat randomZ;

    public RandomVector3f(Vector3f a, Vector3f b) {
        randomX = new RandomFloat(a.x, b.x);
        randomY = new RandomFloat(a.y, b.y);
        randomZ = new RandomFloat(a.z, b.z);
    }

    @Override
    public Vector3f get() {
        return new Vector3f(randomX.get(), randomY.get(), randomZ.get());
    }


}
