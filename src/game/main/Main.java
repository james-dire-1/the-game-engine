package game.main;

import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.common.simulation.collisionEngine.prep.ModelMeshBankInR3;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.rendering.*;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.texturing.TextureBank;
import com.james.renderEngine.ui.Gui;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.utilities.GLUtilities;
import com.james.common.tools.ModelLoader;
import com.james.tools.*;
import game.rendering.Renderers;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Light light = new Light(new Vector3f(0, 10, 0), new Vector3f(1, 1, 1));

    private static long lastTime2 = System.nanoTime()/1000000;
    private static long fps;

    public static void main(String[] args) {
        TextureBank.init("/hello.png", "/stall.png", "/rowdies.png", "/arial.png", "/GameInventorySlot.png",
                "/cobblestone_wall.png", "/white_image.png");
        ModelLoader.init("res/stall.obj", "res/abstract_art.dae", "res/one-sided-wall5.dae", "res/test_environment_7.dae");

        ModelMeshBankInR3.init("res/one-sided-wall5.dae", "res/test_environment_7.dae");
        EllipsoidDimensions.init( new float[][]{ { 1, 1, 1 }, { 0.5f, 3, 0.5f } } );

        FontInfo arial = new FontInfo("/arial.fnt", "/arial.png");
        FontInfo rowdies = new FontInfo("/rowdies.fnt", "/rowdies.png");

        GLFWUtilities.init();
        ParticleHandler.init();

        Renderers.texturedModelRenderer = new TexturedModelRenderer();
        Renderers.colorModelRenderer = new ColorModelRenderer();
        Renderers.flatRenderer = new FlatRenderer();

        MasterRenderer.prepare(Renderers.colorModelRenderer, Renderers.texturedModelRenderer, Renderers.flatRenderer);
        ParticleRenderer.prepare();

        GameLoader gameLoader = new GameLoader();

        Gui gui = new Gui(new ScreenPosition(300, 300), new ScreenSize(100, 100), null);
//        gui.setColors(new float[]{1, 0, 0,   0, 1, 0,   0, 0, 1,   1, 1, 1});
        gui.setTextureAndSamplingData("/GameInventorySlot.png");
        gui.apply();

        List<Gui> guiList = new ArrayList<>();
        guiList.add(gui);

        while (!GLFWUtilities.shouldClose) {
            Time.updateDeltaTime();

            // important stuff
            GLFWUtilities.pollEvents();
            UiHandler.update();
            gameLoader.update();

            // rendering
            gameLoader.render();
            GuiRenderer.render(UiHandler.guisToRender);
            GuiRenderer.render(guiList);
            GLFWUtilities.render();

            // fps timer
            fps++;
            if (System.nanoTime()/1000000 - lastTime2 > 1000) {
                lastTime2 = System.nanoTime()/1000000;
                System.out.println("FPS: " + fps);
                fps = 0;
            }

            ThreadManager.updateMain();
        }

        MasterRenderer.cleanUp();
        ParticleRenderer.cleanUp();
        GuiRenderer.cleanUp();
        GLUtilities.cleanUp();
        GLFWUtilities.cleanUp();
    }

}
