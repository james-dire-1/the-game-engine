package com.james.audio.objects;

import static org.lwjgl.openal.AL10.*;

/**
 * Class for setting properties of the OpenAL listener.
 */
public class AudioListener {

    private static final float[] orientation = { 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f };

    public static void setPosition(float x, float y, float z) {
        alListener3f(AL_POSITION, x, y, z);
    }

    public static void setVelocity(float x, float y, float z) {
        alListener3f(AL_VELOCITY, x, y, z);
    }

    public static void setOrientation(float x, float y, float z) {
        orientation[0] = x;
        orientation[1] = y;
        orientation[2] = z;

        alListenerfv(AL_ORIENTATION, orientation);
    }

}
