package com.james.renderEngine.rendering;

import com.james.renderEngine.gameObjects.Camera;
import com.james.input.WindowResizeInput;
import com.james.tools.BatchedGameObjectsList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class MasterRenderer {

    private static boolean newProjectionMatrix = false;
    public static boolean isNewProjectionMatrix() { return newProjectionMatrix; }

    public static Camera currentCamera = Camera.defaultCamera;

    // TODO consider changing this into an array? is a list really necessary?
    private static final List<AbstractRenderer> renderers = new ArrayList<>();

    public static void prepare(AbstractRenderer... shadersToAdd) {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);

        renderers.addAll(Arrays.asList(shadersToAdd));

        for (AbstractRenderer AbstractRenderer : renderers) {
            AbstractRenderer.prepare();
        }

        WindowResizeInput.addListener(MasterRenderer::onWindowResize);
    }

    private static void onWindowResize() {
        newProjectionMatrix = true;
    }

    public static void render(BatchedGameObjectsList batchedGameObjectsList) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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
