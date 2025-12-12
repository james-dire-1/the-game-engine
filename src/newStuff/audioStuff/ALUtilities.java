package newStuff.audioStuff;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;

public class ALUtilities {

    private static final List<Integer> buffers = new ArrayList<>();

    public static int createBuffer(String audioFilePath) {
        int bufferId = alGenBuffers();
        buffers.add(bufferId);

        AudioLoader audioLoader = AudioLoader.load(audioFilePath);
        alBufferData(bufferId, audioLoader.format, audioLoader.rawAudioBuffer, audioLoader.sampleRate);
        audioLoader.dispose();

        return bufferId;
    }

    public static void cleanUp() {
        for (int buffer : buffers) {
            alDeleteBuffers(buffer);
        }
    }

}
