package game.main;

import com.james.common.simulation.collisionEngine.prep.EllipsoidDimensions;
import com.james.common.simulation.collisionEngine.prep.ModelMeshBankInR3;
import com.james.tools.Time;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.rendering.*;
import com.james.renderEngine.textRendering.FontInfo;
import com.james.renderEngine.texturing.TextureBank;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.renderEngine.utilities.GLUtilities;
import com.james.common.tools.ModelLoader;
import com.james.tools.*;
import game.ui.screens.MultiplayerScreen;
import game.ui.screens.TitleScreen;
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

    private static long lastTime2 = System.nanoTime()/1000000;
    private static long fps;

    public static void main(String[] args) {
        TextureBank.init("/hello.png", "/stall.png", "/rowdies.png", "/arial.png", "/GameInventorySlot.png",
                "/cobblestone_wall.png", "/white_image.png", "/title-button.png", "/dustismo2.png");
        ModelLoader.init("res/stall.obj", "res/abstract_art.dae", "res/one-sided-wall5.dae", "res/test_environment_7.dae");

        ModelMeshBankInR3.init("res/one-sided-wall5.dae", "res/test_environment_7.dae");
        EllipsoidDimensions.init( new float[][]{ { 1, 1, 1 }, { 0.5f, 3, 0.5f } } );

        GLFWUtilities.init();
        ParticleHandler.init();
        TypingInputNotifier.init();

        Renderers.texturedModelRenderer = new TexturedModelRenderer();
        Renderers.colorModelRenderer = new ColorModelRenderer();
        Renderers.flatRenderer = new FlatRenderer();

        MasterRenderer.prepare(Renderers.colorModelRenderer, Renderers.texturedModelRenderer, Renderers.flatRenderer);
        ParticleRenderer.prepare();

        UiHandler.screens.add(new TitleScreen());

        while (!GLFWUtilities.shouldClose) {
            Time.updateDeltaTime();

            // important stuff
            GLFWUtilities.pollEvents();
            UiHandler.update();
            if (gameLoader != null && ClientLevel.get().isReady) gameLoader.update();

            // rendering
            MasterRenderer.preRender();
            if (gameLoader != null && ClientLevel.get().isReady) gameLoader.render();
            GuiRenderer.render(UiHandler.guisToRender);
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
