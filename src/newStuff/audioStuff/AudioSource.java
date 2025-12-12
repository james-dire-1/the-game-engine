package newStuff.audioStuff;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {

    private final int sourceId;

    public AudioSource() {
        sourceId = alGenSources();
    }

    public void play(int bufferId) {
        stop();
        alSourcei(sourceId, AL_BUFFER, bufferId);
        continuePlaying();
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(sourceId);
    }

    public void continuePlaying() {
        alSourcePlay(sourceId);
    }

    public void stop() {
        alSourceStop(sourceId);
    }

    public void cleanUp() {
        stop();
        alDeleteSources(sourceId);
    }

    public void setVolume(float volume) { alSourcef(sourceId, AL_GAIN, volume); }
    public void setPitch(float pitch) { alSourcef(sourceId, AL_PITCH, pitch); }
    public void setLooping(boolean loop) { alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE); }
    public void setPosition(float x, float y, float z) { alSource3f(sourceId, AL_POSITION, x, y, z); }
    public void setVelocity(float x, float y, float z) { alSource3f(sourceId, AL_VELOCITY, x, y, z); }

}
