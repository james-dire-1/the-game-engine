package com.james.renderEngine.rendering.models;

import com.james.renderEngine.gameObjects.Camera;
import com.james.input.WindowResizeInput;
import com.james.tools.BatchedGameObjectsList;

import static org.lwjgl.opengl.GL30.*;

public class MasterRenderer {

    public static final float MIN_BRIGHTNESS = 0.4f;

    private static boolean newProjectionMatrix = false;
    public static boolean isNewProjectionMatrix() { return newProjectionMatrix; }

    public static Camera currentCamera = Camera.defaultCamera;

    private static AbstractRenderer[] renderers;

    public static void prepare(AbstractRenderer... renderersToAdd) {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);

        renderers = new AbstractRenderer[renderersToAdd.length];
        System.arraycopy(renderersToAdd, 0, renderers, 0, renderersToAdd.length);

        for (AbstractRenderer abstractRenderer : renderers) {
            abstractRenderer.prepare();
        }

        WindowResizeInput.addListener(MasterRenderer::onWindowResize);
    }

    private static void onWindowResize() {
        newProjectionMatrix = true;
    }

    public static void preRender() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void render(BatchedGameObjectsList batchedGameObjectsList) {
        for (AbstractRenderer abstractRenderer : renderers) {
            abstractRenderer.render(batchedGameObjectsList);
        }

        newProjectionMatrix = false;
    }

    public static void cleanUp() {
        for (AbstractRenderer AbstractRenderer : renderers) {
            AbstractRenderer.cleanUp();
        }
    }
}
