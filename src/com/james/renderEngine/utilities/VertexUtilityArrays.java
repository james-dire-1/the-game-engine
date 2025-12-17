package com.james.renderEngine.utilities;

import com.james.renderEngine.ui.Gui;

/**
 * This class contains utility arrays for common things, like the vertex positions for guis, particles,
 * text quads, and surface planes. Since it is not sensible to redefine these common arrays for all of
 * these things, they are all placed here, so that all systems can make use of them.
 */
public class VertexUtilityArrays {

    /**
     * Some utility arrays used for constructing the model of a default gui. In addition, these are also
     * used by particles.
     * @see Gui
     * @see com.james.renderEngine.particles.ParticleHandler
     */
    public static final float[] defaultVertexPositions = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, 0.5f, 0,
            0.5f, -0.5f, 0
    };
    public static final int[] defaultIndices = {
            0, 1, 2, 2, 1, 3
    };

    /**
     * Custom vertex positions for use when rendering characters of text. Makes it so that the top left of the
     * quad is the origin, rather than the center, as opposed to the vertex positions of defaultVertexPositions
     * @see com.james.renderEngine.uiElements.GuiText
     */
    public static final float[] textQuadVertexPositions = {
            0, 0, 0,
            0, -1, 0,
            1, 0, 0,
            1, -1, 0
    };

    /**
     * WRITE DOCUMENTATION HERE!
     */
    private static final float SIZE = 500f;
    public static final float[] defaultSkyboxVertexPositions = {
            -SIZE, -SIZE, -SIZE, // 0 bottom left back
            -SIZE, -SIZE, SIZE, // 1 bottom left front
            SIZE, -SIZE, SIZE, // 2 bottom right front
            SIZE, -SIZE, -SIZE, // 3 bottom right back
            -SIZE, SIZE, -SIZE, // 4 top left back
            -SIZE, SIZE, SIZE, // 5 top left front
            SIZE, SIZE, SIZE, // 6 top right front
            SIZE, SIZE, -SIZE, // 7 top right back
    };
    public static final int[] defaultSkyboxIndices = {
            0, 1, 3, 3, 1, 2, // bottom
            5, 1, 4, 4, 1, 0, // left
            7, 3, 6, 6, 3, 2, // right
            4, 0, 7, 7, 0, 3, // back
            6, 2, 5, 5, 2, 1, // front
            5, 4, 6, 6, 4, 7 // top
    };

}
