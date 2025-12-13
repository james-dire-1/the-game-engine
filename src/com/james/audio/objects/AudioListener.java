package com.james.audio.objects;

import static org.lwjgl.openal.AL10.*;

/**
 * Class for setting properties of the OpenAL listener
 */
public class AudioListener {

    public static void setPosition(float x, float y, float z) {
        alListener3f(AL_POSITION, x, y, z);
    }

    public static void setVelocity(float x, float y, float z) {
        alListener3f(AL_VELOCITY, x, y, z);
    }

}
