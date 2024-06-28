package newStuff;

import com.james.common.simulation.LevelProperties;

import java.util.ArrayList;
import java.util.List;

public class ClientLevel extends LevelProperties {

    public final ClientPacketSendEvents events;

    private final List<ConnectedPlayer> connectedPlayers = new ArrayList<>();

    public ClientLevel(ClientPacketSendEvents events) {
        this.events = events;

        singleton = this;
    }

    private static ClientLevel singleton;
    public static ClientLevel get() { return singleton; }

}
