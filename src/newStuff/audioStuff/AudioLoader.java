package newStuff.audioStuff;

import templates.common.GlobalConstants;

import java.nio.ByteBuffer;

public abstract class AudioLoader {

    public int format;
    public ByteBuffer rawAudioBuffer;
    public int sampleRate;

    public abstract void dispose();

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
