package com.james.renderEngine.rendering;

import com.james.renderEngine.gameObjects.GameObject;
import com.james.input.WindowResizeInput;
import com.james.math.Mth;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.shaders.FlatShader;
import com.james.tools.BatchedGameObjectsList;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL30.*;

public class FlatRenderer extends AbstractRenderer {

    private final FlatShader shader = new FlatShader();

    @Override
    public void prepare() {
        shader.start();
        Matrix4f projectionMatrix = Mth.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    @Override
    public void render(BatchedGameObjectsList batchedGameObjectsList) {
        shader.start();

        if (MasterRenderer.isNewProjectionMatrix()) {
            Matrix4f projectionMatrix = Mth.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
            shader.loadProjectionMatrix(projectionMatrix);
        }

        shader.loadViewMatrix(MasterRenderer.currentCamera.getViewMatrix());

        for (Model model : batchedGameObjectsList.getGameObjectsMap().keySet()) {
            if (!models.contains(model)) continue;

            RawModel rawModel = model.rawModel;

            glBindVertexArray(rawModel.vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, model.getTexture().id);

            for (GameObject gameObject : batchedGameObjectsList.getGameObjectsMap().get(model)) {
                Matrix4f transformationMatrix = Mth.createTransformationMatrix(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale());
                shader.loadTransformationMatrix(transformationMatrix);

                // code for checking for index buffer is not used, since the satisfiesModelCriteria
                // method checks that the model uses an index buffer
                // Therefore, glDrawElements will always be used over glDrawArrays

                glDrawElements(GL_TRIANGLES, rawModel.vertexCount, GL_UNSIGNED_INT, 0);
            }

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
        }

        shader.stop();
    }

    @Override
    public boolean satisfiesModelCriteria(Model model) {
        return model.hasTexture() && model.rawModel.usesIndexBuffer;
    }

    @Override
    public void cleanUp() {
        shader.cleanUp();
    }

}
