package com.james.tools;

import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.visuals.LightSettings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LightHandler {

    private final Map<Integer, Light> idsToLightsMap = new HashMap<>();

    public void addLight(int id, Light light) {
        idsToLightsMap.put(id, light);
        LightSettings.onLightAdded();
    }

    public boolean removeLight(int id) {
        Light removedLight = idsToLightsMap.remove(id);
        boolean success = removedLight != null;
        if (success) LightSettings.onLightRemoved();
        return success;
    }

    public Light getLight(int id) {
        return idsToLightsMap.get(id);
    }

    public Collection<Light> getLights() {
        return idsToLightsMap.values();
    }

    public int numLights() {
        return idsToLightsMap.size();
    }

}
