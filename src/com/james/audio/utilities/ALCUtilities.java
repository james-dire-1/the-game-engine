package com.james.audio.utilities;

import org.lwjgl.openal.*;

import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Utility class that handles the initialization and cleaning up of OpenAL.
 */
public class ALCUtilities {

    private static long device;
    private static long context;

    public static void init() {
        String defaultDeviceName = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        if (device == NULL) throw new RuntimeException("Cannot open default OpenAL device");

        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) throw new RuntimeException("Failed to create OpenAL context");
        alcMakeContextCurrent(context);

        ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
        AL.createCapabilities(deviceCapabilities);
    }

    public static void cleanUp() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

}
