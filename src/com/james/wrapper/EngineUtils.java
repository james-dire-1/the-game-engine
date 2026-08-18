package com.james.wrapper;

import com.james.audio.AudioSourcePool;
import com.james.audio.utilities.ALCUtilities;
import com.james.audio.utilities.ALUtilities;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.rendering.GuiRenderer;
import com.james.renderEngine.rendering.ParticleRenderer;
import com.james.renderEngine.rendering.SkyboxRenderer;
import com.james.renderEngine.rendering.models.AbstractRenderer;
import com.james.renderEngine.rendering.models.MasterRenderer;
import com.james.renderEngine.ui.TypingInputNotifier;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.utilities.GLUtilities;
import com.james.simulation.ClientLevel;
import com.james.tools.ThreadManager;
import com.james.tools.Time;
import com.james.gameplay.GameLoader;

public class EngineUtils {

    public static GameLoader gameLoader;

    public static int fps;
    private static long lastTime = System.nanoTime() / 1000000;
    private static int fpsAccumulator;

    public static void init(String windowTitle) {
        GLFWUtilities.init(windowTitle);
        ParticleHandler.init();
        TypingInputNotifier.init();
        ALCUtilities.init();
        AudioSourcePool.init();
    }

    public static void initRenderers(AbstractRenderer... renderersToAdd) {
        MasterRenderer.prepare(renderersToAdd);
        ParticleRenderer.prepare();
        SkyboxRenderer.prepare();
        GuiRenderer.prepare();
    }

    public static void tick() {
        Time.updateDeltaTime();
        GLFWUtilities.pollEvents();
        UiHandler.update();
        if (gameLoader != null && ClientLevel.get().isReady) gameLoader.update();
        ThreadManager.updateMain();
    }

    public static void render() {
        MasterRenderer.preRender();
        if (gameLoader != null && ClientLevel.get().isReady) gameLoader.render();
        GuiRenderer.render(UiHandler.guisToRender);
        GLFWUtilities.render();
    }

    public static void updateFpsInfo() {
        fpsAccumulator++;
        if (System.nanoTime() / 1000000 - lastTime > 1000) {
            lastTime = System.nanoTime() / 1000000;
            fps = fpsAccumulator;
            fpsAccumulator = 0;
        }
    }

    public static void cleanUp() {
        ALUtilities.cleanUp();
        ALCUtilities.cleanUp();
        MasterRenderer.cleanUp();
        ParticleRenderer.cleanUp();
        SkyboxRenderer.cleanUp();
        GuiRenderer.cleanUp();
        GLUtilities.cleanUp();
        GLFWUtilities.cleanUp();
    }

}
