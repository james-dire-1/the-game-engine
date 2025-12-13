package com.james.audio.objects;

import static org.lwjgl.openal.AL10.*;

/**
 * Wrapper around an OpenAL source. Contains the basic functionality you'd expect from OpenAL sources.
 */
// TODO: 2025-12-12 The fact that we're creating whole objects just to store a single id is kind of inefficient
// TODO: 2025-12-12 Perhaps this needs to be redone with a less object-oriented approach
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

    public void setRelative() { alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE); }
    public void setVolume(float volume) { alSourcef(sourceId, AL_GAIN, volume); }
    public void setPitch(float pitch) { alSourcef(sourceId, AL_PITCH, pitch); }
    public void setLooping(boolean loop) { alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE); }
    public void setPosition(float x, float y, float z) { alSource3f(sourceId, AL_POSITION, x, y, z); }
    public void setVelocity(float x, float y, float z) { alSource3f(sourceId, AL_VELOCITY, x, y, z); }

}
