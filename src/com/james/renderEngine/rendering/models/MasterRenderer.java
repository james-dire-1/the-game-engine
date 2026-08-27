package com.james.renderEngine.rendering.models;

import com.james.renderEngine.gameObjects.Camera;
import com.james.input.WindowResizeInput;
import com.james.renderEngine.visuals.LightSettings;
import com.james.tools.BatchedGameObjectsList;
import com.james.renderEngine.visuals.FogSettings;
import templates.settings.UserSettings;

import static org.lwjgl.opengl.GL30.*;

public class MasterRenderer {

    private static boolean newProjectionMatrix = false;
    public static boolean isNewProjectionMatrix() { return newProjectionMatrix; }
    // TODO: 2026-08-26 All calls to this method seem to create a completely new projection matrix..

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

        drawCalls = drawCallsAccumulator;
        drawCallsAccumulator = 0;

        triangles = trianglesAccumulator;
        trianglesAccumulator = 0;
    }

    public static void render(BatchedGameObjectsList batchedGameObjectsList) {
        for (AbstractRenderer abstractRenderer : renderers) {
            abstractRenderer.render(batchedGameObjectsList);
        }

        newProjectionMatrix = false;
        LightSettings.resetState();
        FogSettings.resetState();
    }

    public static void cleanUp() {
        for (AbstractRenderer AbstractRenderer : renderers) {
            AbstractRenderer.cleanUp();
        }
    }

    public static void setFov(float fov) {
        UserSettings.fov = fov;
        newProjectionMatrix = true;
    }

    public static int drawCalls;
    private static int drawCallsAccumulator;
    public static void incrementDrawCalls() {
        drawCallsAccumulator++;
    }

    public static int triangles;
    private static int trianglesAccumulator;
    public static void increaseTriangles(int triangles) {
        trianglesAccumulator += triangles;
    }

}
