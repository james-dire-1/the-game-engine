package com.james.audio.objects;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;

/**
 * Wrapper around an OpenAL source. Contains the basic functionality you'd expect from OpenAL sources.
 */
public class AudioSource {

    public final int sourceId;
    private boolean playing;

    public AudioSource() {
        sourceId = alGenSources();

        alSourcef(sourceId, AL_ROLLOFF_FACTOR, 1.0f);
        alSourcef(sourceId, AL_REFERENCE_DISTANCE, 2.0f);
        alSourcef(sourceId, AL_MAX_DISTANCE, 1000.0f);
    }

    public void play(int bufferId) {
        stop();

        if (delay >= 0.01f) {
            alSourceRewind(sourceId);
            alSourcef(sourceId, AL_SEC_OFFSET, delay);
        }

        alSourcei(sourceId, AL_BUFFER, bufferId);
        continuePlaying();
    }

    public boolean isPlaying() {
        return playing;
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

    private float delay;
    public void setDelay(Float seconds) {
        this.delay = seconds;
    }

    public void setRelative(boolean relative) { alSourcei(sourceId, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE); }
    public void setVolume(float volume) { alSourcef(sourceId, AL_GAIN, volume); }
    public void setPitch(float pitch) { alSourcef(sourceId, AL_PITCH, pitch); }
    public void setLooping(boolean loop) { alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE); }
    public void setPosition(float x, float y, float z) { alSource3f(sourceId, AL_POSITION, x, y, z); }
    public void setVelocity(float x, float y, float z) { alSource3f(sourceId, AL_VELOCITY, x, y, z); }

    /**
     * Updates the playing field. Should be called once per frame. This allows the isPlaying() method to
     * always return the same result for a given frame, as opposed to not having the playing middleman.
     * (Recall that OpenAL operates completely independently of GLFW and so has no knowledge of the game loop,
     * etc.)
     */
    public void updateIsPlayingProperty() {
        this.playing = alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

}
