package com.james.renderEngine.rendering;

import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.tools.RenderingMath;
import com.james.renderEngine.shaders.GuiShader;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.GuiMeshData;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class GuiRenderer {

    private static GuiShader shader;

    public static void prepare() {
        shader = new GuiShader();
    }

    public static void render(List<Gui> guis) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);

        shader.start();

        for (int priority = 1; priority <= Gui.getMaxPriority(); priority++) {
            for (Gui gui : guis) {
                if (!gui.isVisible || gui.getPriority() != priority) continue;

                GuiMeshData mesh = gui.getMesh();

                glBindVertexArray(mesh.vaoId);
                glEnableVertexAttribArray(0);

                if (gui.renderingMode == Gui.RenderingMode.Texture ||
                        gui.renderingMode == Gui.RenderingMode.Text ||
                        gui.renderingMode == Gui.RenderingMode.TextWithColorBuffer ||
                        gui.renderingMode == Gui.RenderingMode.TextureAndSingleColor) {
                    glEnableVertexAttribArray(1);

                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, gui.getTexture().id);
                }

                if (gui.renderingMode == Gui.RenderingMode.ColorGradient ||
                        gui.renderingMode == Gui.RenderingMode.TextWithColorBuffer)
                    glEnableVertexAttribArray(2);

                Matrix4f transformationMatrix = RenderingMath.createTransformationMatrix(gui.position.normalized(), gui.size.normalized());
                shader.loadTransformationMatrix(transformationMatrix);

                shader.loadRenderingMode(gui.renderingMode);

                if (gui.renderingMode == Gui.RenderingMode.SingleColor ||
                        gui.renderingMode == Gui.RenderingMode.Text ||
                        gui.renderingMode == Gui.RenderingMode.TextureAndSingleColor)
                    shader.loadSingleColor(gui.singleColor);

                if (gui.renderingMode == Gui.RenderingMode.ColorGradient ||
                        gui.renderingMode == Gui.RenderingMode.SingleColor ||
                        gui.renderingMode == Gui.RenderingMode.Text)
                    shader.loadAlpha(gui.alpha);

                glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0);
                MasterRenderer.incrementDrawCalls();
                MasterRenderer.increaseTriangles(mesh.vertexCount / 3);

                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
                glDisableVertexAttribArray(2);
                glBindVertexArray(0);
            }
        }

        shader.stop();

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    public static void cleanUp() {
        shader.cleanUp();
    }

}
