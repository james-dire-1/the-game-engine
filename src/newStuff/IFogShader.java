package newStuff;

import org.lwjgl.util.vector.Vector3f;

public interface IFogShader {

    void loadSkyColor(Vector3f skyColor);
    void loadFogType(boolean useSphericalFog);
    void loadFogDensity(float fogDensity);
    void loadFogGradient(float fogGradient);
    void loadFogApplied(boolean fogApplied);
    void loadCameraPosition(Vector3f cameraPosition);

}
