package com.james.renderEngine.shaders.models.interfaces;

import com.james.renderEngine.shaders.models.components.FogShaderFeature;
import org.lwjgl.util.vector.Vector3f;

public interface IFogShader {

    FogShaderFeature fog();

    default void loadSkyColor(Vector3f skyColor) {
        fog().loadSkyColor(skyColor);
    }

    default void loadFogType(boolean useSphericalFog) {
        fog().loadFogType(useSphericalFog);
    }

    default void loadFogDensity(float fogDensity) {
        fog().loadFogDensity(fogDensity);
    }

    default void loadFogGradient(float fogGradient) {
        fog().loadFogGradient(fogGradient);
    }

    default void loadFogApplied(boolean fogApplied) {
        fog().loadFogApplied(fogApplied);
    }

    default void loadCameraPosition(Vector3f cameraPosition) {
        fog().loadCameraPosition(cameraPosition);
    }

}
