package newStuff.audioStuff;

import static org.lwjgl.openal.AL10.*;

public class AudioListener {

    public static void setProperties(float x, float y, float z) {
        alListener3f(AL_POSITION, x, y, z);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

}
