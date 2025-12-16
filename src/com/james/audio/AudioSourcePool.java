package com.james.audio;

import com.james.audio.objects.AudioSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpful class for playing relative sounds whenever, which abstracts away the idea of OpenAL sources.
 * Contains a pool of sources so that multiple relative sounds can be played at any one time.
 */
public class AudioSourcePool {

    private static final int INITIAL_COUNT = 3;
    private static final int MAX_COUNT = 100;

    private static final List<AudioSource> sources = new ArrayList<>();

    /**
     * Creates the initial pool of sources. Should be called at the beginning of the game.
     */
    public static void init() {
        for (int i = 0; i < INITIAL_COUNT; i++) {
            sources.add(new AudioSource());
        }
    }

    /**
     * Plays a relative sound without delay. Calls the more general play() below.
     */
    public static void play(int bufferId) {
        play(bufferId, null);
    }

    /**
     * Plays a relative sound with delay.
     *
     * @implNote Loops through the sources in the pool until it finds one that currently isn't playing a sound,
     * and uses that source to play the sound. If no free source was found, a new source will be created to
     * play the sound, provided that we don't already have MAX_COUNT sources. If we do, then the sound isn't
     * played at all.
     */
    public static void play(int bufferId, Float delay) {
        boolean searching = true;
        boolean hasPlayedSound = false;
        int currentIndex = 0;

        while (searching) {
            if (currentIndex < sources.size()) {
                AudioSource source = sources.get(currentIndex);
                currentIndex++;

                if (!source.isPlaying()) {
                    source.setDelay(delay != null ? delay : 0);
                    source.play(bufferId);
                    hasPlayedSound = true;
                    searching = false;
                }
            } else {
                searching = false;
            }
        }

        if (!hasPlayedSound && sources.size() < MAX_COUNT) {
            AudioSource source = new AudioSource();
            sources.add(source);
            source.setDelay(delay != null ? delay : 0);
            source.play(bufferId);
        }
    }

}
