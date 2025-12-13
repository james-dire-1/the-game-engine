package com.james.audio.loaders;

import templates.common.GlobalConstants;

import java.nio.ByteBuffer;

/**
 * Class that handles extracting the necessary info from a sound file that is needed for creating OpenAL
 * buffers. This info is stored in this class's fields for ease of retrieval after. These fields are common for
 * both sound files in .wav format and in .ogg format. This class is inherited by WavLoader and OggLoader,
 * which contain functionality specific to their respective file types.
 *
 * @see WavLoader
 * @see OggLoader
 */
public abstract class AudioLoader {

    public int format;
    public ByteBuffer rawAudioBuffer;
    public int sampleRate;

    /**
     * Common clean up method that is meant to be called after creating the OpenAL buffer.
     */
    public abstract void dispose();

    /**
     * Takes in a sound file and returns an AudioLoader object with populated fields.
     *
     * @implNote if the file is in .wav, we call WavLoader.create(), which does all the heavy lifting for .wav
     * files, and if the file is in .ogg, we call OggLoader.extractInfo(), which does all the heavy lifting for
     * .ogg files.
     */
    public static AudioLoader load(String path) {
        AudioLoader audioLoader = null;
        if (path.endsWith(".wav")) {
            String fullPath = GlobalConstants.AUDIO_BASE_DIRECTORY_WAV + path;
            audioLoader = WavLoader.create(fullPath);
        } else if (path.endsWith(".ogg")) {
            String fullPath = GlobalConstants.AUDIO_BASE_DIRECTORY_OGG + path;
            audioLoader = OggLoader.extractInfo(fullPath);
        }

        return audioLoader;
    }

}
