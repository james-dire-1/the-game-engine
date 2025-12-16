package com.james.audio;

import com.james.audio.utilities.ALUtilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that keeps track of a map of sound file paths to their respective OpenAL buffer IDs. IDs for buffers
 * should generally be obtained via this class.
 */
public class AudioBank {

    private static final Map<String, Integer> bufferIdMap = new HashMap<>();

    /**
     * Gets the buffer ID for the given sound file path. If a buffer doesn't already exist for that sound, it
     * will be created.
     */
    public static int getOrCreate(String path) {
        if (!bufferIdMap.containsKey(path)) {
            int bufferId = ALUtilities.createBuffer(path);
            bufferIdMap.put(path, bufferId);
        }

        return bufferIdMap.get(path);
    }

}
