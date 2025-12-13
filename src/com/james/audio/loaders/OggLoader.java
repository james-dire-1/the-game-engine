package com.james.audio.loaders;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * Audio loader class specifically for .ogg files. Unlike the WavLoader class for .wav, which implements a lot
 * of the logic for opening up .wav files, this class is really just a glorified wrapper for STBVorbis, which
 * really only works for .ogg files. The code in here (and particularly in the extractInfo() method) was taken
 * from a tutorial video by GamesWithGabe: // https://www.youtube.com/watch?v=dLrqBTeipwg
 */
public class OggLoader extends AudioLoader {

    /**
     * Constructor used internally for populating the fields that will be referred to when creating OpenAL
     * buffers. Determining what these fields should be is done in extractInfo() below.
     */
    private OggLoader(int format, ShortBuffer shortRawAudioBuffer, int sampleRate) {
        this.format = format;
        this.rawAudioBuffer = memAlloc(shortRawAudioBuffer.remaining() * 2);
        this.sampleRate = sampleRate;

        while (shortRawAudioBuffer.hasRemaining()) {
            rawAudioBuffer.putShort(shortRawAudioBuffer.get());
        }
        rawAudioBuffer.flip();
    }

    /**
     * Called from AudioLoader's load() method. Does the heavy lifting for turning a .ogg file into the fields
     * we need for OpenAL buffers.
     */
    public static OggLoader extractInfo(String path) {
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer shortRawAudioBuffer = stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer);

        if (shortRawAudioBuffer == null) {
            System.out.println("Could not load sound " + path);
            stackPop();
            stackPop();
            return null;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        stackPop();
        stackPop();

        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        return new OggLoader(format, shortRawAudioBuffer, sampleRate);
    }

    /**
     * Clean up method to be called after creating the OpenAL buffer.
     */
    @Override
    public void dispose() {
        memFree(rawAudioBuffer);
        // free(shortRawAudioBuffer);
        // I want to free the shortRawAudioBuffer, but I keep getting crashes!
    }

}
