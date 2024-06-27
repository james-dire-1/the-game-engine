package com.james.renderEngine.rendering;

import com.james.tools.RenderingMath;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.shaders.BasicShader;
import com.james.tools.BatchedGameObjectsList;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL30.*;

public class BasicRenderer {

    private final BasicShader shader = new BasicShader();

    public void prepare() {
        shader.start();
        Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(BatchedGameObjectsList batchedGameObjectsList) {
        shader.start();

        if (MasterRenderer.isNewProjectionMatrix()) {
            Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
            shader.loadProjectionMatrix(projectionMatrix);
        }

        shader.loadViewMatrix(MasterRenderer.currentCamera.getViewMatrix());

        shader.loadTime();

        for (Model model : batchedGameObjectsList.getGameObjectsMap().keySet()) {
            RawModel rawModel = model.rawModel;

            glBindVertexArray(rawModel.vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, model.getTexture().id);

            for (GameObject gameObject : batchedGameObjectsList.getGameObjectsMap().get(model)) {
                Matrix4f transformationMatrix = RenderingMath.createTransformationMatrix(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale());
                shader.loadTransformationMatrix(transformationMatrix);

                glDrawElements(GL_TRIANGLES, rawModel.vertexCount, GL_UNSIGNED_INT, 0);
            }

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
        }

        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

}
