package com.james.renderEngine.rendering.models;

import com.james.tools.RenderingMath;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.models.Model;
import com.james.renderEngine.models.RawModel;
import com.james.renderEngine.shaders.models.ColorModelShader;
import com.james.tools.BatchedGameObjectsList;
import game.main.Main;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL30.*;

public class ColorModelRenderer extends AbstractRenderer {

    private final ColorModelShader shader = new ColorModelShader();

    @Override
    public void prepare() {
        shader.start();
        Matrix4f projectionMatrix = RenderingMath.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadMinBrightness(MasterRenderer.MIN_BRIGHTNESS);
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
        shader.loadLight(Main.light);

        for (Model model : batchedGameObjectsList.getGameObjectsMap().keySet()) {
            if (!models.contains(model)) continue;

            if (!model.usesCulling()) glDisable(GL_CULL_FACE);

            RawModel rawModel = model.rawModel;

            glBindVertexArray(rawModel.vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            for (GameObject gameObject : batchedGameObjectsList.getGameObjectsMap().get(model)) {
                if (!gameObject.isVisible) continue;

                Matrix4f transformationMatrix = RenderingMath.createTransformationMatrix(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale());
                shader.loadTransformationMatrix(transformationMatrix);

                shader.loadShineSettings(model.getShineSettings());

                if (rawModel.usesIndexBuffer) {
                    glDrawElements(GL_TRIANGLES, rawModel.vertexCount, GL_UNSIGNED_INT, 0);
                } else {
                    glDrawArrays(GL_TRIANGLES, 0, rawModel.vertexCount);
                }
            }

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glBindVertexArray(0);

            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }

        shader.stop();
    }

    @Override
    public boolean satisfiesModelCriteria(Model model) {
        return model.hasColors() && model.hasNormals();
    }

    @Override
    public void cleanUp() {
        shader.cleanUp();
    }

}
