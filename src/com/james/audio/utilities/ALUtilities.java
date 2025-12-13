package com.james.audio.utilities;

import com.james.audio.loaders.AudioLoader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;

/**
 * Utility class for handling audio buffers in OpenAL
 */
public class ALUtilities {

    private static final List<Integer> buffers = new ArrayList<>();

    /**
     * Creates a new OpenAL buffer to store sound data.
     * @param audioFilePath can in .wav or .ogg
     *
     * @return id of the buffer
     */
    public static int createBuffer(String audioFilePath) {
        int bufferId = alGenBuffers();
        buffers.add(bufferId);

        AudioLoader audioLoader = AudioLoader.load(audioFilePath);
        alBufferData(bufferId, audioLoader.format, audioLoader.rawAudioBuffer, audioLoader.sampleRate);
        audioLoader.dispose();

        return bufferId;
    }

    /**
     * Deletes all OpenAL buffers when closing the game.
     */
    public static void cleanUp() {
        for (int buffer : buffers) {
            alDeleteBuffers(buffer);
        }
    }

}
