package newStuff.audioStuff;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class OggLoader extends AudioLoader {

    private OggLoader(int format, ShortBuffer shortRawAudioBuffer, int sampleRate) {
        this.format = format;
        this.rawAudioBuffer = memAlloc(shortRawAudioBuffer.remaining() * 2);
        this.sampleRate = sampleRate;

        while (shortRawAudioBuffer.hasRemaining()) {
            rawAudioBuffer.putShort(shortRawAudioBuffer.get());
        }
        rawAudioBuffer.flip();
    }

    // https://www.youtube.com/watch?v=dLrqBTeipwg
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

    @Override
    public void dispose() {
        memFree(rawAudioBuffer);
        // free(shortRawAudioBuffer);
        // TODO: 2025-12-12 I want to free the shortRawAudioBuffer, but I keep getting crashes!
    }

}
