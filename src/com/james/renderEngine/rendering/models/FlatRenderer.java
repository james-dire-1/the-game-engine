package com.james.renderEngine.rendering.models;

import com.james.tools.RenderingMath;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.shaders.models.FlatShader;
import com.james.tools.BatchedGameObjectsList;
import com.james.renderEngine.visuals.FogSettings;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL30.*;

public class FlatRenderer extends AbstractRenderer {

    private final FlatShader shader = new FlatShader();

    @Override
    public void prepare() {
        shader.start();
        Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        shader.loadProjectionMatrix(projectionMatrix);
        FogSettings.loadSettingsFirstTime(shader);
        shader.stop();
    }

    @Override
    public void render(BatchedGameObjectsList batchedGameObjectsList) {
        shader.start();

        if (MasterRenderer.isNewProjectionMatrix()) {
            Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
            shader.loadProjectionMatrix(projectionMatrix);
        }

        shader.loadViewMatrix(MasterRenderer.currentCamera.getViewMatrix());
        FogSettings.loadSettings(shader);
        shader.loadCameraPosition(MasterRenderer.currentCamera.getPosition());

        for (Model model : batchedGameObjectsList.getGameObjectsMap().keySet()) {
            if (!models.contains(model)) continue;

            if (!model.usesCulling()) glDisable(GL_CULL_FACE);

            RawModel rawModel = model.rawModel;

            glBindVertexArray(rawModel.vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, model.getTexture().id);

            for (GameObject gameObject : batchedGameObjectsList.getGameObjectsMap().get(model)) {
                if (!gameObject.isVisible) continue;

                Matrix4f transformationMatrix = RenderingMath.createTransformationMatrix(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale());
                shader.loadTransformationMatrix(transformationMatrix);

                shader.loadFogApplied(gameObject.isAffectedByFog);

                if (rawModel.usesIndexBuffer) {
                    glDrawElements(GL_TRIANGLES, rawModel.vertexCount, GL_UNSIGNED_INT, 0);
                } else {
                    glDrawArrays(GL_TRIANGLES, 0, rawModel.vertexCount);
                }
            }

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }

        shader.stop();
    }

    @Override
    public boolean satisfiesModelCriteria(Model model) {
        return model.hasTexture();
    }

    @Override
    public void cleanUp() {
        shader.cleanUp();
    }

}
