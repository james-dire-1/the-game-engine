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

/**
 * A single, general-purpose gui element.
 */
public class Gui {

    public final float[] vertexPositions;
    private final int[] indices;
    private float[] textureCoords;
    private float[] colors;
    public float[] singleColor;
    public float alpha = 1.0f;
    private boolean isText = false;

    private GuiMeshData mesh;
    public GuiMeshData getMesh() { return mesh; }

    private ImageTexture texture;
    public ImageTexture getTexture() { return texture; }

    public Position position;
    public Size size;

    // TODO: 2024-12-27 Does this necessarily have to be final?
    public final Gui parent;
    // TODO: 2025-06-23 Consider making the ArrayList only if necessary for efficiency
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
    // TODO: 2024-12-24 Also, there should be some check to see if this method is being called for a gui that is
    // TODO: 2024-12-24 using conventional vertexPositions and indices arrays
    public void setTextureAndSamplingData(String path, int x, int y, int widthToSample, int heightToSample, boolean normalized) {
        float normalizedX;
        float normalizedY;
        float normalizedWidthToSample;
        float normalizedHeightToSample;

        if (!normalized) {
            BufferedImage image = TextureBank.getTexture(path);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

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
     * Overload of setTextureAndSamplingData() to be used for unconventional texture coordinates. Useful for
     * text rendering.
     */
    public void setTextureAndSamplingData(String path, float[] textureCoords) {
        this.textureCoords = textureCoords;
        this.texture = ImageTexture.getOrCreateImageTexture(path);
    }

    /**
     * Sets the gui to use colors at each vertex.
     */
    public void setColors(float[] colors) {
        this.colors = colors;
    }

    /**
     * Sets the gui to use a single color. Thus, instead of assigning a color to each vertex, which has
     * to be loaded into a VAO, the single color is passed to the shader as a uniform variable.
     * @implNote There is a separate array to be used for a single color, because the real colors array
     * gets passed to GuiMeshData, which will load it into a VAO. This is of course not what we want for
     * a single color.
     */
    // TODO: 2025-06-23 This is inefficient! We are creating new arrays every time this is called
    public void setSingleColor(float r, float g, float b) {
        this.singleColor = new float[] {r, g, b};
    }

    /**
     * Marks the gui (whether it corresponds to a single character, or a whole string of text) as text. This
     * is important for the GuiRenderer to know, as rendering for text guis is different compared to other types
     * of guis
     */
    public void markAsText() {
        isText = true;
    }

    /**
     * Once all the data for this gui has been gathered, it can be applied to receive its GuiMeshData instance.
     * The reason this happens at the end is so that all the information can be tested against all the GuiMeshData
     * instances' information that already exists, and if one GuiMeshData instance's information is identical
     * to the information we have here, then it can simply be returned without any redundant new GuiMeshData
     * instance getting created. Hence, "getOrCreateGuiMeshData"
     * Not only that, but we need to know everything about the gui before we can assign its RenderingMode, which
     * also happens in this method.
     */
    public void apply() {
        if (textureCoords != null && colors == null && singleColor == null && !isText)
            renderingMode = RenderingMode.Texture; // 1
        else if (textureCoords == null && colors != null && singleColor == null && !isText)
            renderingMode = RenderingMode.ColorGradient; // 2
        else if (textureCoords == null && colors == null && singleColor != null && !isText)
            renderingMode = RenderingMode.SingleColor; // 3
        else if (textureCoords != null && colors == null && singleColor != null)
            if (isText) renderingMode = RenderingMode.Text; // 4
            else renderingMode = RenderingMode.TextureAndSingleColor; // 6
        else if (textureCoords != null && colors != null && singleColor == null && isText)
            renderingMode = RenderingMode.TextWithColorBuffer; // 5
        else
            throw new RuntimeException();

        this.mesh = GuiMeshData.getOrCreateGuiMeshData(vertexPositions, indices, textureCoords, colors);
    }

    public enum RenderingMode {
        Texture, ColorGradient, SingleColor, Text, TextWithColorBuffer, TextureAndSingleColor
    }

}
