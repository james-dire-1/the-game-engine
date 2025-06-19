package com.james.renderEngine.models;

import com.james.renderEngine.rendering.AbstractRenderer;
import com.james.renderEngine.texturing.ImageTexture;
import com.james.renderEngine.utilities.GLUtilities;

/**
 * Represents a model with enough information for rendering. Includes the vertex positions (RawModel),
 * and any optional data including indices, textures, texture coordinates, colors, normals, etc.
 */
public class Model {

    public final RawModel rawModel;

    private ImageTexture texture;
    private float[] textureCoords;
    public boolean hasTexture() { return texture != null; }
    public ImageTexture getTexture() { return texture; }

    private float[] colors;
    public boolean hasColors() { return colors != null; }

    private float[] normals;
    public boolean hasNormals() { return normals != null; }

    private boolean culling = true;
    public boolean usesCulling() { return culling; }

    /**
     * Unlike vertexCount in RawModel, uniqueVertexCount counts NON-REPEATING vertices. Used only for
     * debugging.
     */
    private final int uniqueVertexCount;

    private boolean hasRendererBeenSet = false;

    /**
     * Create a new Model with specified vertex positions and indices. Assumes that any other per-vertex
     * data supplied to this model later (ex. texture coords) will use the indices buffer.
     */
    public Model(float[] vertexPositions, int[] indices) {
        this.rawModel = new RawModel(vertexPositions, indices);
        this.uniqueVertexCount = vertexPositions.length / 3;
    }

    /**
     * Create a new Model with specified vertex positions without indices buffer. Assumes that any other
     * per-vertex data supplied to this model later (ex. texture coords) will not need an indices buffer either.
     */
    public Model(float[] vertexPositions) {
        this.rawModel = new RawModel(vertexPositions);
        this.uniqueVertexCount = vertexPositions.length / 3;
    }

    public void setTextureAndTextureCoords(String path, float[] textureCoords) {
        this.texture = ImageTexture.getOrCreateImageTexture(path);
        this.textureCoords = textureCoords;

        GLUtilities.bindVAO(rawModel.vaoId);
        GLUtilities.storeDataInVAO(1, 2, textureCoords);
        GLUtilities.unbindBoundVAO();

        errorChecking(DataType.TEXTURE_COORDS);
    }

    public void setColors(float[] colors) {
        this.colors = colors;

        GLUtilities.bindVAO(rawModel.vaoId);
        GLUtilities.storeDataInVAO(1, 3, colors);
        GLUtilities.unbindBoundVAO();

        errorChecking(DataType.COLORS);
    }

    public void setNormals(float[] normals) {
        this.normals = normals;

        GLUtilities.bindVAO(rawModel.vaoId);
        GLUtilities.storeDataInVAO(2, 3, normals);
        GLUtilities.unbindBoundVAO();

        errorChecking(DataType.NORMALS);
    }

    public void disableCulling() {
        culling = false;
    }

    /**
     * Sets the renderer that this model should be rendered with. This should be called after all
     * modifications to the model have already been made, since this method checks that all necessary
     * criteria are met to use the requested renderer. Calling this method is necessary for your model to
     * appear on the screen.
     */
    public void setRenderer(AbstractRenderer renderer) {
        if (hasRendererBeenSet) throw new RuntimeException();

        hasRendererBeenSet = true;

        if (!renderer.satisfiesModelCriteria(this)) throw new RuntimeException("Model doesn't satisfy the renderer's criteria!");

        renderer.models.add(this);
    }

    private enum DataType { TEXTURE_COORDS, COLORS, NORMALS }

    /**
     * Makes sure nothing out of the ordinary occurs
     */
    private void errorChecking(DataType floatType) {
        if (floatType == DataType.TEXTURE_COORDS) {
            if ((float) textureCoords.length / 2 != (float) uniqueVertexCount) {
                throw new RuntimeException();
            }
        }
        else if (floatType == DataType.COLORS) {
            if ((float) colors.length / 3 != (float) uniqueVertexCount) {
                throw new RuntimeException();
            }
        }
        else if (floatType == DataType.NORMALS) {
            if ((float) normals.length / 3 != (float) uniqueVertexCount) {
                throw new RuntimeException();
            }
        }
    }

}
