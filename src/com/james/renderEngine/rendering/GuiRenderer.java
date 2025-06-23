package com.james.renderEngine.rendering;

import com.james.tools.RenderingMath;
import com.james.renderEngine.shaders.GuiShader;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.GuiMeshData;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class GuiRenderer {

    private static final GuiShader shader = new GuiShader();

    public static void render(List<Gui> guis) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        shader.start();

        for (Gui gui : guis) {
            if (!gui.isVisible) continue;

            GuiMeshData mesh = gui.getMesh();

            glBindVertexArray(mesh.vaoId);
            glEnableVertexAttribArray(0);

            if (    gui.renderingMode == Gui.RenderingMode.Texture ||
                    gui.renderingMode == Gui.RenderingMode.Text ||
                    gui.renderingMode == Gui.RenderingMode.TextWithColorBuffer)
                glEnableVertexAttribArray(1);

            if (    gui.renderingMode == Gui.RenderingMode.ColorGradient ||
                    gui.renderingMode == Gui.RenderingMode.TextWithColorBuffer)
                glEnableVertexAttribArray(2);

            if (    gui.renderingMode == Gui.RenderingMode.Texture ||
                    gui.renderingMode == Gui.RenderingMode.Text ||
                    gui.renderingMode == Gui.RenderingMode.TextWithColorBuffer) {
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, gui.getTexture().id);
            }

            Matrix4f transformationMatrix = RenderingMath.createTransformationMatrix(gui.position.normalized(), gui.size.normalized());
            shader.loadTransformationMatrix(transformationMatrix);

            shader.loadRenderingMode(gui.renderingMode);

            if (    gui.renderingMode == Gui.RenderingMode.SingleColor ||
                    gui.renderingMode == Gui.RenderingMode.Text)
                shader.loadSingleColor(gui.singleColor);

            glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glBindVertexArray(0);
        }

        shader.stop();

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    public static void cleanUp() {
        shader.cleanUp();
    }

}
