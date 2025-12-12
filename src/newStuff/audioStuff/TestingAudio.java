package newStuff.audioStuff;

public class TestingAudio {

    public static void main(String[] args) throws InterruptedException {
        ALCUtilities.init();
        AudioListener.setProperties();

        int bufferId = ALUtilities.createBuffer("/bounce.ogg");
        AudioSource source = new AudioSource();
        source.play(bufferId);

        Thread.sleep(5000);

        source.cleanUp();
        ALUtilities.cleanUp();
        ALCUtilities.cleanUp();
    }

}
