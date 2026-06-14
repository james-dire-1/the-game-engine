package com.james.audio;

import com.james.audio.objects.AudioSource;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * Helpful class for playing positional and relative sounds whenever, which abstracts away the idea of OpenAL
 * sources. Contains a pool of sources so that multiple sounds can be played at any one time.
 */
public class AudioSourcePool {

    private static final int INITIAL_COUNT = 10;
    private static final int MAX_COUNT = 100;

    private static final Map<Integer, AudioSource> sources = new HashMap<>();

    /**
     * Creates the initial pool of sources. Should be called at the beginning of the game.
     */
    public static void init() {
        for (int i = 0; i < INITIAL_COUNT; i++) {
            AudioSource source = new AudioSource();
            sources.put(source.sourceId, source);
        }
    }

    /**
     * Updates the playing property of all AudioSource instances. Should be called once per frame.
     */
    public static void update() {
        for (AudioSource source : sources.values()) {
            source.updateIsPlayingProperty();
        }
    }

    /**
     * Plays a relative sound with delay. Calls the more general play() below.
     */
    public static void playRelative(int bufferId, float delay) {
        play(bufferId, delay, null, null);
    }

    /**
     * Plays a positional or relative sound with delay.
     *
     * @param position if null is passed in, the sound will be played as a relative sound; if a valid Vector3f
     *                 is passed in, the sound will be played at the given position
     * @param velocity the velocity of the sound source to simulate the Doppler effect; if null, no Doppler
     *                 effect; this velocity is considered regardless of whether the sound is to be played as
     *                 positional or relative
     *
     * @return the source id so that the caller can continue to manipulate the sound source via methods that
     * require the source id; -1 is returned if no audio source is available (i.e. all sources are currently
     * playing a sound and MAX_COUNT sources has been reached)
     *
     * @implNote Loops through the sources in the pool until it finds one that currently isn't playing a sound,
     * and uses that source to play the sound. If no free source was found, a new source will be created to
     * play the sound, provided that we don't already have MAX_COUNT sources. If we do, then the sound isn't
     * played at all.
     */
    public static int play(int bufferId, float delay, Vector3f position, Vector3f velocity) {
        for (AudioSource source : sources.values()) {
            if (!source.isPlaying()) {
                setSourceProperties(source, bufferId, delay, position, velocity);
                return source.sourceId;
            }
        }

        if (sources.size() < MAX_COUNT) {
            AudioSource source = new AudioSource();
            sources.put(source.sourceId, source);
            setSourceProperties(source, bufferId, delay, position, velocity);

            return source.sourceId;
        }

        return -1;
    }

    /**
     * Sets the important properties for the given audio source and plays the given sound. This method is
     * defined to avoid code duplication in play() above.
     */
    private static void setSourceProperties(AudioSource source, int bufferId, float delay, Vector3f position, Vector3f velocity) {
        if (position != null) {
            source.setPosition(position.x, position.y, position.z);
            source.setRelative(false);
        } else {
            source.setRelative(true);
        }

        if (velocity != null) {
            source.setVelocity(velocity.x, velocity.y, velocity.z);
        }

        source.setDelay(delay);
        source.play(bufferId);
    }

    /**
     * Changes the position of the given sound. It only makes sense to call this method for positional sounds
     * (i.e. non-relative sounds). Note also that this method should not be called on sounds that are already
     * done being played (check the return value). This is because once a sound is done being played, its
     * audio source is considered as available to play a different sound, and so the sound no longer has
     * ownership over the audio source.
     *
     * @return whether the sound is done being played (i.e. whether you should no longer change the sound's
     * position)
     */
    public static boolean changePosition(int sourceId, Vector3f position) {
        AudioSource source = sources.get(sourceId);

        if (source.isPlaying()) {
            source.setPosition(position.x, position.y, position.z);
            return false;
        }

        return true;
    }

    /**
     * Changes the velocity of the given sound. This method can be called for positional and relative sounds.
     * Note also that this method should not be called on sounds that are already done being played (check the
     * return value).
     *
     * @return whether the sound is done being played (i.e. whether you should no longer change the sound's
     * velocity)
     */
    public static boolean changeVelocity(int sourceId, Vector3f velocity) {
        AudioSource source = sources.get(sourceId);

        if (source.isPlaying()) {
            source.setVelocity(velocity.x, velocity.y, velocity.z);
            return false;
        }

        return true;
    }

}
