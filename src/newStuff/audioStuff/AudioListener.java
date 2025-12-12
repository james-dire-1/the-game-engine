package newStuff.audioStuff;

import static org.lwjgl.openal.AL10.*;

public class AudioListener {

    public static void setProperties() {
        alListener3f(AL_POSITION, 0, 0, 0);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

}
