package game.communication;

public class Warnings {

    public static void printClientServerDeSyncWarning(String warning) {
        System.out.println("WARNING: CLIENT SERVER DE-SYNC HAS BEEN DETECTED: " + warning);
    }

}
