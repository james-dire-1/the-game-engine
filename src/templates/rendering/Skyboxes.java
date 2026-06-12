package templates.rendering;

import com.james.renderEngine.visuals.Skybox;

import java.util.HashMap;
import java.util.Map;

public class Skyboxes {

    public static final String DEFAULT_SKYBOX_NAME = "sky gradient";
    private static final Map<String, Skybox> skyboxes = new HashMap<>();

    public static void init() {
        skyboxes.put("sky with clouds", new Skybox("sky with clouds"));
        skyboxes.put("sky gradient", new Skybox("sky gradient"));
    }

    public static Skybox getByName(String name) {
        return skyboxes.get(name);
    }

}
