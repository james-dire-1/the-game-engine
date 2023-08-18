package com.james.renderEngine.particles;

import com.james.renderEngine.utilities.VertexUtilityArrays;
import com.james.renderEngine.utilities.GLUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleHandler {

    public static final float GRAVITY = -5;
    public static final List<Particle> particles = new ArrayList<>();

    private static int vaoId;
    public static int getVaoId() {
        if (vaoId == 0)
            throw new RuntimeException("You forgot to call ParticleHandler.init()!");

        return vaoId;
    }

    private static int vertexCount;
    public static int getVertexCount() {
        if (vertexCount == 0)
            throw new RuntimeException("You forgot to call ParticleHandler.init()!");

        return vertexCount;
    }

    public static void init() {
        vaoId = GLUtilities.createAndBindVAO();
        vertexCount = VertexUtilityArrays.defaultIndices.length;

        GLUtilities.storeIndicesDataInVAO(VertexUtilityArrays.defaultIndices);
        GLUtilities.storeDataInVAO(0, 3, VertexUtilityArrays.defaultVertexPositions);
        GLUtilities.unbindBoundVAO();
    }

    public static void update() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            
            boolean shouldBeRemoved = particle.update();
            if (shouldBeRemoved) {
                iterator.remove();
            }
        }
    }

}
