package com.james.tools;

import com.james.renderEngine.gameObjects.DirectionalLight;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.visuals.LightSettings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LightHandler {

    private final Map<Integer, Light> lightsMap = new HashMap<>();
    private final Map<Integer, DirectionalLight> directionalLightsMap = new HashMap<>();

    public void addLight(int id, Light light) {
        lightsMap.put(id, light);
        LightSettings.onLightAdded();
    }

    public void addDirectionalLight(int id, DirectionalLight directionalLight) {
        directionalLightsMap.put(id, directionalLight);
        LightSettings.onDirectionalLightAdded();
    }

    public boolean removeLight(int id) {
        Light removedLight = lightsMap.remove(id);
        boolean success = removedLight != null;
        if (success) LightSettings.onLightRemoved();
        return success;
    }

    public boolean removeDirectionalLight(int id) {
        DirectionalLight removedDirectionalLight = directionalLightsMap.remove(id);
        boolean success = removedDirectionalLight != null;
        if (success) LightSettings.onDirectionalLightRemoved();
        return success;
    }

    public Light getLight(int id) {
        return lightsMap.get(id);
    }

    public DirectionalLight getDirectionalLight(int id) {
        return directionalLightsMap.get(id);
    }

    public Collection<Light> getLights() {
        return lightsMap.values();
    }

    public Collection<DirectionalLight> getDirectionalLights() {
        return directionalLightsMap.values();
    }

    public int numLights() {
        return lightsMap.size();
    }

    public int numDirectionalLights() {
        return directionalLightsMap.size();
    }

}
