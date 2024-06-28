package newStuff;

public class LocalServerProperties implements ServerProperties {

    public ConnectedPlayer connectedPlayer;
    private static final Object lock = new Object();

    @Override
    public void playerJoinedReceived(ConnectedPlayer connectedPlayer) {
        synchronized (lock) {
            this.connectedPlayer = connectedPlayer;
        }
    }

}
