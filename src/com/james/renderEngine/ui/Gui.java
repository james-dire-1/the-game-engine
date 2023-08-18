package com.james.renderEngine.ui;

import com.james.renderEngine.texturing.ImageTexture;
import com.james.renderEngine.texturing.TextureBank;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.MixedPosition;
import com.james.renderEngine.ui.dataTypes.Position;
import com.james.renderEngine.ui.dataTypes.Size;
import com.james.renderEngine.utilities.VertexUtilityArrays;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Gui {

    public final float[] vertexPositions;
    private final int[] indices;
    private float[] textureCoords;

    private GuiMeshData mesh;
    public GuiMeshData getMesh() { return mesh; }

    private ImageTexture texture;
    public ImageTexture getTexture() { return texture; }

    public Position position;
    public Size size;

    public final Gui parent;
    private final List<Gui> children = new ArrayList<>();

    /**
     * Overload constructor that uses the default vertex positions for guis.
     */
    public Gui(Position position, Size size, Gui parent) {
        this(position, size, parent, VertexUtilityArrays.defaultVertexPositions, VertexUtilityArrays.defaultIndices);
    }

    // TODO: 2022-12-14 Consider optimizing this code, since new models are created every time a new gui is made.
    // TODO: 2022-12-14 While this might be necessary for guis of different textures, it is not necessary that
    // TODO: 2022-12-14 a new model is created every time a new gui without a texture is created

    /**
     * Creates a gui object with specified position, size, parent, and vertex positions.
     */
    public Gui(Position position, Size size, Gui parent, float[] vertexPositions, int[] indices) {
        this.position = position;
        this.size = size;
        this.parent = parent;
        this.vertexPositions = vertexPositions;
        this.indices = indices;

        position.setGui(this);
        size.setGui(this);

        if (parent != null) {
            parent.children.add(this);
        }
        if (position instanceof MixedPosition) {
            ((MixedPosition) position).setGuiForMembers(this);
        } else if (position instanceof AnchoredPosition) {
            ((AnchoredPosition) position).setSize(size);
        }
    }

    /**
     * Sets the gui to use a texture. Also takes in info for sampling the texture in the case that only
     * a section of the texture is desired to be rendered.
     * @implNote x, y, widthToSample, and heightToSample are measured in pixels, but OpenGL expects the data
     * to be normalized, so first we must normalize the data if it isn't normalized already
     */
    public void setTextureAndSamplingData(String path, int x, int y, int widthToSample, int heightToSample, boolean normalized) {
        BufferedImage image = TextureBank.getTexture(path);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        float normalizedX;
        float normalizedY;
        float normalizedWidthToSample;
        float normalizedHeightToSample;

        if (!normalized) {
            normalizedX = (float) x / imageWidth;
            normalizedY = (float) y / imageHeight;
            normalizedWidthToSample = (float) widthToSample / imageWidth;
            normalizedHeightToSample = (float) heightToSample / imageHeight;
        } else {
            normalizedX = x;
            normalizedY = y;
            normalizedWidthToSample = widthToSample;
            normalizedHeightToSample = heightToSample;
        }

        this.textureCoords = new float[]{
                normalizedX, normalizedY,
                normalizedX, normalizedY + normalizedHeightToSample,
                normalizedX + normalizedWidthToSample, normalizedY,
                normalizedX + normalizedWidthToSample, normalizedY + normalizedHeightToSample
        };
        this.texture = ImageTexture.getOrCreateImageTexture(path);
    }

    /**
     * Overload of setTextureAndSamplingData to be used when the whole texture should be applied to gui.
     */
    public void setTextureAndSamplingData(String path) {
        setTextureAndSamplingData(path, 0, 0, 1, 1, true);
    }

    /**
     * Once all the data for this gui has been gathered, it can be applied to receive its GuiMeshData instance.
     * The reason this happens at the end is so that all the information can be tested against all the GuiMeshData
     * instances' information that already exists, and if one GuiMeshData instance's information is identical
     * to the information we have here, then it can simply be returned without any new redundant new GuiMeshData
     * instance getting created. Hence, "getOrCreateGuiMeshData"
     */
    public void apply() {
        this.mesh = GuiMeshData.getOrCreateGuiMeshData(vertexPositions, indices, textureCoords);
    }

}
