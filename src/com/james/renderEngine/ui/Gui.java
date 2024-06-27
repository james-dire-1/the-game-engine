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
import java.util.Arrays;
import java.util.List;

public class Gui {

    public final float[] vertexPositions;
    private final int[] indices;
    private float[] textureCoords;
    private float[] colors;
    public float[] singleColor;

    private GuiMeshData mesh;
    public GuiMeshData getMesh() { return mesh; }

    private ImageTexture texture;
    public ImageTexture getTexture() { return texture; }

    public Position position;
    public Size size;

    public final Gui parent;
    public final List<Gui> children = new ArrayList<>();

    public RenderingMode renderingMode;
    public boolean isVisible = true;
    public boolean isEnabled = true;

    /**
     * Overload constructor that uses the default vertex positions for guis.
     */
    public Gui(Position position, Size size, Gui parent) {
        this(position, size, parent, VertexUtilityArrays.defaultVertexPositions, VertexUtilityArrays.defaultIndices);
    }

    /**
     * Creates a gui object with specified position, size, parent, vertex positions, colors, and indices.
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
        if (renderingMode != null)
            throw new RuntimeException();

        renderingMode = RenderingMode.Texture;

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
     * Overload of setTextureAndSamplingData() to be used when the whole texture should be applied to gui.
     */
    public void setTextureAndSamplingData(String path) {
        setTextureAndSamplingData(path, 0, 0, 1, 1, true);
    }

    /**
     * Sets the gui to use colors at each vertex.
     */
    public void setColors(float[] colors) {
        if (renderingMode != null)
            throw new RuntimeException();

        renderingMode = RenderingMode.ColorGradient;

        this.colors = colors;
    }

    /**
     * Sets the gui to use a single color. Thus, instead of assigning a color to each vertex, which has
     * to be loaded into a VAO, the single color is passed to the shader as a uniform variable.
     * @implNote There is a separate array to be used for a single color, because the real colors array
     * gets passed to GuiMeshData, which will load it into a VAO. This is of course not what we want for
     * a single color.
     */
    public void setSingleColor(float r, float g, float b) {
        if (renderingMode != null)
            throw new RuntimeException();

        renderingMode = RenderingMode.SingleColor;

        this.singleColor = new float[] {r, g, b};
    }

    /**
     * Once all the data for this gui has been gathered, it can be applied to receive its GuiMeshData instance.
     * The reason this happens at the end is so that all the information can be tested against all the GuiMeshData
     * instances' information that already exists, and if one GuiMeshData instance's information is identical
     * to the information we have here, then it can simply be returned without any new redundant new GuiMeshData
     * instance getting created. Hence, "getOrCreateGuiMeshData"
     */
    public void apply() {
        this.mesh = GuiMeshData.getOrCreateGuiMeshData(vertexPositions, indices, textureCoords, colors);
    }

    public enum RenderingMode {
        Texture, ColorGradient, SingleColor
    }

}
