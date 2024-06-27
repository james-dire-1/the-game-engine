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
        glDisable(GL_DEPTH_TEST);

        shader.start();

        for (Gui gui : guis) {
            if (!gui.isVisible) continue;

            GuiMeshData mesh = gui.getMesh();

            glBindVertexArray(mesh.vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gui.getTexture().id);

            Matrix4f transformationMatrix = RenderingMath.createTransformationMatrix(gui.position.normalized(), gui.size.normalized());
            shader.loadTransformationMatrix(transformationMatrix);

            glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
        }

        shader.stop();

        glEnable(GL_DEPTH_TEST);
    }

    public static void cleanUp() {
        shader.cleanUp();
    }

}
