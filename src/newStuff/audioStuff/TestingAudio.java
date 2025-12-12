package newStuff.audioStuff;

import java.io.IOException;
import java.util.Scanner;

public class TestingAudio {

    public static void main(String[] args) throws IOException {
        ALCUtilities.init();
        AudioListener.setProperties(0, 0, 0);

        int bufferId = ALUtilities.createBuffer("/bounce.wav");
        AudioSource source = new AudioSource();
        source.setLooping(true);
        source.play(bufferId);

        AudioSource source2 = new AudioSource();
        source2.setPitch(2);

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("stop")) {
            input = scanner.nextLine();

            if (input.equals("p")) {
                if (source.isPlaying()) {
                    source.pause();
                } else {
                    source.continuePlaying();
                }
            }

            if (input.equals("o")) {
                source2.play(bufferId);
            }
        }

        source.cleanUp();
        source2.cleanUp();
        ALUtilities.cleanUp();
        ALCUtilities.cleanUp();
    }

}
