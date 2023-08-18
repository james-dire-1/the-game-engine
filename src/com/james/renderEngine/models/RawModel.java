package com.james.renderEngine.models;

import com.james.renderEngine.utilities.GLUtilities;

public class RawModel {

    public final int vaoId;
    public final int vertexCount;
    public final boolean usesIndexBuffer;
    public final float[] vertexPositions; // used for precise collision detections

    public RawModel(float[] vertexPositions, int[] indices) {
        this.vaoId = GLUtilities.createAndBindVAO();
        this.vertexCount = indices.length;
        this.usesIndexBuffer = true;
        this.vertexPositions = vertexPositions;

        GLUtilities.storeIndicesDataInVAO(indices);
        GLUtilities.storeDataInVAO(0, 3, vertexPositions);
        GLUtilities.unbindBoundVAO();
    }

    public RawModel(float[] vertexPositions) {
        this.vaoId = GLUtilities.createAndBindVAO();
        this.vertexCount = vertexPositions.length / 3;
        this.usesIndexBuffer = false;
        this.vertexPositions = vertexPositions;

        GLUtilities.storeDataInVAO(0, 3, vertexPositions);
        GLUtilities.unbindBoundVAO();
    }

}
