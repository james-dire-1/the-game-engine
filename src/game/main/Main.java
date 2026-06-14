package game.main;

import com.james.audio.AudioSourcePool;
import com.james.audio.utilities.ALCUtilities;
import com.james.audio.utilities.ALUtilities;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.common.simulation.collisionEngine.prep.ModelMeshBankInR3;
import com.james.renderEngine.rendering.models.*;
import com.james.tools.Time;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.rendering.*;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.texturing.ImageBank;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.utilities.GLUtilities;
import com.james.tools.*;
import game.ui.screens.TitleScreen;
import com.james.renderEngine.texturing.CubeMapTexture;
import com.james.renderEngine.visuals.Skybox;
import com.james.renderEngine.rendering.SkyboxRenderer;
import com.james.common.tools.modelLoading.ModelLoader;
import com.james.renderEngine.rendering.models.TextureBlendModelRenderer;
import templates.rendering.Skyboxes;
import templates.settings.GLFWWindowTitles;
import templates.gameplay.GameLoader;
import com.james.renderEngine.ui.TypingInputNotifier;
import templates.rendering.Renderers;
import com.james.simulation.ClientLevel;

public class Main {

    public static GameLoader gameLoader;

    public static final FontInfo arial = new FontInfo("/arial.fnt", "/arial.png");
    public static final FontInfo rowdies = new FontInfo("/rowdies.fnt", "/rowdies.png");
    public static final FontInfo dustismo = new FontInfo("/dustismo.fnt", "/dustismo.png");

    public static int fps;
    private static long lastTime = System.nanoTime() / 1000000;
    private static int fpsAccumulator;

    public static void main(String[] args) {
        ImageBank.init("/textures/misc/red-explosive.png", "/textures/objects/stall.png", "/fonts/rowdies.png", "/fonts/arial.png",
                "/textures/misc/inventory-slot.png", "/textures/objects/cobblestone-wall.png", "/textures/misc/white-image.png", "/textures/ui/title-button.png",
                "/fonts/dustismo.png", "/textures/skyboxes/skyWithClouds/right.png", "/textures/skyboxes/skyWithClouds/left.png",
                "/textures/skyboxes/skyWithClouds/top.png", "/textures/skyboxes/skyWithClouds/bottom.png", "/textures/skyboxes/skyWithClouds/back.png",
                "/textures/skyboxes/skyWithClouds/front.png", "/textures/scenes/desertScene/stone-ground.png",
                "/textures/skyboxes/skyGradient/top.png", "/textures/skyboxes/skyGradient/bottom.png", "/textures/skyboxes/skyGradient/side.png",
                "/textures/scenes/beachScene/black-and-pink-stone.png", "/textures/scenes/beachScene/palm-bark-and-wood.png", "/textures/scenes/beachScene/palm-bark.png",
                "/textures/scenes/beachScene/palm-leaves.png", "/textures/scenes/beachScene/palm-leaves-2.png", "/textures/scenes/beachScene/palm-tree.png",
                "/textures/scenes/beachScene/pebbles.png", "/textures/scenes/beachScene/purple.png", "/textures/scenes/beachScene/red.png", "/textures/scenes/beachScene/sand.png",
                "/textures/scenes/beachScene/stone.png", "/textures/scenes/beachScene/water.png", "/textures/scenes/beachScene/wood.png", "/textures/scenes/beachScene/yellow.png",
                "/textures/scenes/plainsScene/grass.png", "/textures/scenes/plainsScene/thin-matrix-blend-map.png", "/textures/scenes/plainsScene/thin-matrix-flowers.png",
                "/textures/scenes/plainsScene/thin-matrix-grass.png", "/textures/scenes/plainsScene/thin-matrix-mud.png", "/textures/scenes/plainsScene/thin-matrix-path.png");
        ModelLoader.init("/objects/stall.obj", "/objects/abstract-art.dae", "/objects/one-sided-wall.dae", "/scenes/test-scene.dae",
                "/scenes/desert-scene.dae", "/scenes/beach-scene.dae", "/scenes/plains-scene.dae");

        ModelMeshBankInR3.init("/objects/one-sided-wall.dae", "/scenes/test-scene.dae", "/scenes/desert-scene.dae", "/scenes/beach-scene.dae", "/scenes/plains-scene.dae");
        EllipsoidDimensions.init( new float[][]{ { 1, 1, 1 }, { 0.5f, 3, 0.5f } } );

        GLFWUtilities.init(GLFWWindowTitles.MAIN);
        ParticleHandler.init();
        TypingInputNotifier.init();
        ALCUtilities.init();
        AudioSourcePool.init();

        CubeMapTexture.create("sky with clouds", "/textures/skyboxes/skyWithClouds", new String[] {"right", "left", "top", "bottom", "back", "front"});
        CubeMapTexture.create("sky gradient", "/textures/skyboxes/skyGradient", new String[] {"side", "side", "top", "bottom", "side", "side"});

        Renderers.init();
        Skyboxes.init();

        MasterRenderer.prepare(Renderers.basicRenderer, Renderers.flatRenderer, Renderers.texturedModelRenderer, Renderers.colorModelRenderer, Renderers.textureBlendModelRenderer);
        ParticleRenderer.prepare();
        SkyboxRenderer.prepare();
        GuiRenderer.prepare();

        UiHandler.screens.add(new TitleScreen());

        while (!GLFWUtilities.shouldClose) {
            Time.updateDeltaTime();

            // important stuff
            GLFWUtilities.pollEvents();
            UiHandler.update();
            if (gameLoader != null && ClientLevel.get().isReady) gameLoader.update();

            ThreadManager.updateMain();

            // rendering
            MasterRenderer.preRender();
            if (gameLoader != null && ClientLevel.get().isReady) gameLoader.render();
            GuiRenderer.render(UiHandler.guisToRender);
            GLFWUtilities.render();

            AudioSourcePool.update();

            // fps timer
            fpsAccumulator++;
            if (System.nanoTime() / 1000000 - lastTime > 1000) {
                lastTime = System.nanoTime() / 1000000;
                System.out.println("FPS: " + fpsAccumulator);
                fps = fpsAccumulator;
                fpsAccumulator = 0;
            }
        }

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
