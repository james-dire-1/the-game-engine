package com.james.renderEngine.rendering;

import com.james.input.WindowResizeInput;
import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.renderEngine.visuals.Skybox;
import com.james.tools.RenderingMath;
import com.james.renderEngine.shaders.SkyboxShader;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL30.*;

public class SkyboxRenderer {

    private static SkyboxShader shader;

    public static void prepare() {
        shader = new SkyboxShader();
        shader.start();
        Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public static void render(Skybox skybox) {
        glDisable(GL_DEPTH_TEST);
        shader.start();

        if (MasterRenderer.isNewProjectionMatrix()) {
            Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
            shader.loadProjectionMatrix(projectionMatrix);
        }

        shader.loadViewMatrix(MasterRenderer.currentCamera.getViewMatrix(), skybox.unmoving);

        RawModel rawModel = Skybox.getRawModel();
        glBindVertexArray(rawModel.vaoId);
        glEnableVertexAttribArray(0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, skybox.texture.id);

        glDrawElements(GL_TRIANGLES, rawModel.vertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.stop();

        glEnable(GL_DEPTH_TEST);
    }

    public static void cleanUp() {
        shader.cleanUp();
    }

}
