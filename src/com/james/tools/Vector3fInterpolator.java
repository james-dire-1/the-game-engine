package com.james.tools;

import org.lwjgl.util.vector.Vector3f;

public class Vector3fInterpolator {

    public static float secondsPerGameTick;

    public static void interpolate(Vector3f start, Vector3f end, Vector3f result, float lastTime, float currentTime) {
        float normalizedProgress = Math.min((currentTime - lastTime) / secondsPerGameTick, 1f);

        float xRange = end.x - start.x;
        float yRange = end.y - start.y;
        float zRange = end.z - start.z;

        result.x = start.x + normalizedProgress * xRange;
        result.y = start.y + normalizedProgress * yRange;
        result.z = start.z + normalizedProgress * zRange;
    }

}
