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

}
