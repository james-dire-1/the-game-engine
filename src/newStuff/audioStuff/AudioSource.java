package newStuff.audioStuff;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {

    private final int sourceId;

    public AudioSource() {
        sourceId = alGenSources();
        alSourcef(sourceId, AL_GAIN, 1);
        alSourcef(sourceId, AL_PITCH, 1);
        alSource3f(sourceId, AL_POSITION, 0, 0, 0);
    }

    public void play(int bufferId) {
        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcePlay(sourceId);
    }

    public void cleanUp() {
        alDeleteSources(sourceId);
    }

}
