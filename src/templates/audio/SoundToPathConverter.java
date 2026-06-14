package templates.audio;

import templates.common.audio.Sound;

public class SoundToPathConverter {

    public static String convert(Sound sound) {
        String soundPath = null;

        if (sound == Sound.CLICK) {
            soundPath = "/click.wav";
        }

        return soundPath;
    }

}
