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
import game.ui.screens.TitleScreen;
import newStuff.UsernamePromptScreen;
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

    private static long lastTime2 = System.nanoTime()/1000000;
    private static long fps;

    public static void main(String[] args) {
        TextureBank.init("/textures/red-explosive.png", "/textures/stall.png", "/fonts/rowdies.png", "/fonts/arial.png", "/textures/inventory-slot.png",
                "/textures/cobblestone-wall.png", "/textures/white-image.png", "/textures/title-button.png", "/fonts/dustismo2.png");
        ModelLoader.init("/stall.obj", "/abstract-art.dae", "/one-sided-wall.dae", "/test-environment.dae");

        ModelMeshBankInR3.init("/one-sided-wall.dae", "/test-environment.dae");
        EllipsoidDimensions.init( new float[][]{ { 1, 1, 1 }, { 0.5f, 3, 0.5f } } );

        GLFWUtilities.init(GLFWWindowTitles.MAIN);
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

            ThreadManager.updateMain();

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
        }

        MasterRenderer.cleanUp();
        ParticleRenderer.cleanUp();
        GuiRenderer.cleanUp();
        GLUtilities.cleanUp();
        GLFWUtilities.cleanUp();
    }

}
