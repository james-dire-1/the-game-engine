package com.james.renderEngine.ui;

import com.james.renderEngine.utilities.GLUtilities;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains the mesh data for rendering guis. Similar to the Model class, except for guis.
 * This class is used for guis instead of the Model class because the Model class is overkill for guis.
 * This class' contents are used in the GuiRenderer.
 * @see com.james.renderEngine.models.Model
 * @see com.james.renderEngine.rendering.GuiRenderer
 */
public class GuiMeshData {

    public final int vaoId;
    public final int vertexCount;

    private GuiMeshData(float[] vertexPositions, int[] indices, float[] textureCoords, float[] colors) {
        this.vaoId = GLUtilities.createAndBindVAO();
        this.vertexCount = indices.length;

        GLUtilities.storeIndicesDataInVAO(indices);
        GLUtilities.storeDataInVAO(0, 3, vertexPositions);
        if (textureCoords != null) GLUtilities.storeDataInVAO(1, 2, textureCoords);
        if (colors != null) GLUtilities.storeDataInVAO(2, 3, colors);

        GLUtilities.unbindBoundVAO();
    }

    /**
     * Map that stores mesh data with its corresponding GuiMeshData instance.
     */
    private static final Map<Data, GuiMeshData> guiMeshDataMap = new HashMap<>();

    /**
     * Gets the GuiMeshData instance corresponding to the info passed in, or creates a new instance if a matching
     * one doesn't already exist. This method is designed this way to avoid unnecessary instances of GuiMeshData,
     * which in turn means less vaos with duplicate data (the constructor of GuiMeshData creates a new vao every
     * time).
     */
    public static GuiMeshData getOrCreateGuiMeshData(float[] vertexPositions, int[] indices, float[] textureCoords, float[] colors) {
        for (Data data : guiMeshDataMap.keySet()) {
            if (    Arrays.equals(data.vertexPositions, vertexPositions) &&
                    Arrays.equals(data.indices, indices) &&
                    Arrays.equals(data.textureCoords, textureCoords) &&
                    Arrays.equals(data.colors, colors)) {
                return guiMeshDataMap.get(data);
            }
        }

        GuiMeshData guiMeshData = new GuiMeshData(vertexPositions, indices, textureCoords, colors);
        guiMeshDataMap.put(new Data(vertexPositions, indices, textureCoords, colors), guiMeshData);

        return guiMeshData;
    }

    /**
     * Container class used for storing all the info that will need to be in the key set of the guiMeshDataMap.
     */
    private static class Data {
        private final float[] vertexPositions;
        private final int[] indices;
        private final float[] textureCoords;
        private final float[] colors;

        private Data(float[] vertexPositions, int[] indices, float[] textureCoords, float[] colors) {
            this.vertexPositions = vertexPositions;
            this.indices = indices;
            this.textureCoords = textureCoords;
            this.colors = colors;
        }
    }

}
