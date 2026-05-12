package game.main;

import com.james.audio.AudioSourcePool;
import com.james.audio.utilities.ALCUtilities;
import com.james.audio.utilities.ALUtilities;
import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.common.simulation.collisionEngine.prep.ModelMeshBankInR3;
import com.james.renderEngine.rendering.models.*;
import com.james.tools.Time;
import com.james.renderEngine.gameObjects.Light;
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
import com.james.renderEngine.skyboxes.Skybox;
import com.james.renderEngine.rendering.SkyboxRenderer;
import com.james.common.tools.modelLoading.ModelLoader;
import templates.settings.GLFWWindowTitles;
import templates.gameplay.GameLoader;
import com.james.renderEngine.ui.TypingInputNotifier;
import templates.rendering.Renderers;
import com.james.simulation.ClientLevel;
import org.lwjgl.util.vector.Vector3f;

public class Main {

    public static final Light light = new Light(new Vector3f(0, 10, 0), new Vector3f(1, 1, 1));

    public static GameLoader gameLoader;

    public static final FontInfo arial = new FontInfo("/arial.fnt", "/arial.png");
    public static final FontInfo rowdies = new FontInfo("/rowdies.fnt", "/rowdies.png");
    public static final FontInfo dustismo = new FontInfo("/dustismo2.fnt", "/dustismo2.png");

    private static long lastTime = System.nanoTime() / 1000000;
    private static long fps;

    public static void main(String[] args) {
        ImageBank.init("/textures/red-explosive.png", "/textures/stall.png", "/fonts/rowdies.png", "/fonts/arial.png",
                "/textures/inventory-slot.png", "/textures/cobblestone-wall.png", "/textures/white-image.png", "/textures/title-button.png",
                "/fonts/dustismo2.png", "/textures/skyboxes/beautiful-sky/right.png", "/textures/skyboxes/beautiful-sky/left.png",
                "/textures/skyboxes/beautiful-sky/top.png", "/textures/skyboxes/beautiful-sky/bottom.png", "/textures/skyboxes/beautiful-sky/back.png",
                "/textures/skyboxes/beautiful-sky/front.png", "/textures/stone-ground.png", "/textures/skyboxes/beautiful-sky-2/beautiful-sky-2.png",
                "/textures/skyboxes/beautiful-sky-3/top.png", "/textures/skyboxes/beautiful-sky-3/bottom.png", "/textures/skyboxes/beautiful-sky-3/side.png",
                "/textures/skyboxes/beautiful-sky-4/top.png", "/textures/skyboxes/beautiful-sky-4/bottom.png", "/textures/skyboxes/beautiful-sky-4/side.png",
                "/textures/beach-scene/black-and-pink-stone.png", "/textures/beach-scene/palm-bark-and-wood.png", "/textures/beach-scene/palm-bark.png",
                "/textures/beach-scene/palm-leaves.png", "/textures/beach-scene/palm-leaves-2.png", "/textures/beach-scene/palm-tree.png",
                "/textures/beach-scene/pebbles.png", "/textures/beach-scene/purple.png", "/textures/beach-scene/red.png", "/textures/beach-scene/sand.png",
                "/textures/beach-scene/stone.png", "/textures/beach-scene/water.png", "/textures/beach-scene/wood.png", "/textures/beach-scene/yellow.png");
        ModelLoader.init("/stall.obj", "/abstract-art.dae", "/one-sided-wall.dae", "/test-environment.dae", "/desert-2.dae",
                "/blender-test-7.dae", "/beach-scene.dae");

        ModelMeshBankInR3.init("/one-sided-wall.dae", "/test-environment.dae", "/desert-2.dae", "/beach-scene.dae");
        EllipsoidDimensions.init( new float[][]{ { 1, 1, 1 }, { 0.5f, 3, 0.5f } } );

        GLFWUtilities.init(GLFWWindowTitles.MAIN);
        ParticleHandler.init();
        TypingInputNotifier.init();
        ALCUtilities.init();
        AudioSourcePool.init();

        CubeMapTexture.create("beautiful sky"  , "/textures/skyboxes/beautiful-sky", new String[] {"right", "left", "top", "bottom", "back", "front"});
        CubeMapTexture.create("beautiful sky 2", "/textures/skyboxes/beautiful-sky-2/beautiful-sky-2.png");
        CubeMapTexture.create("beautiful sky 3", "/textures/skyboxes/beautiful-sky-3", new String[] {"side", "side", "top", "bottom", "side", "side"});
        CubeMapTexture.create("beautiful sky 4", "/textures/skyboxes/beautiful-sky-4", new String[] {"side", "side", "top", "bottom", "side", "side"});
        Skybox.currentSkybox = new Skybox("beautiful sky 4");
        Skybox.currentSkybox.unmoving = false;

        Renderers.basicRenderer = new BasicRenderer();
        Renderers.flatRenderer = new FlatRenderer();
        Renderers.texturedModelRenderer = new TexturedModelRenderer();
        Renderers.colorModelRenderer = new ColorModelRenderer();

        MasterRenderer.prepare(Renderers.basicRenderer, Renderers.flatRenderer, Renderers.colorModelRenderer, Renderers.texturedModelRenderer);
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

            // fps timer
            fps++;
            if (System.nanoTime() / 1000000 - lastTime > 1000) {
                lastTime = System.nanoTime() / 1000000;
                System.out.println("FPS: " + fps);
                fps = 0;
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
