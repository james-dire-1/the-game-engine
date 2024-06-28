package newStuff;

import org.lwjgl.util.vector.Vector3f;

public class ServerPacketReceiveActions {

    // TODO: 2024-06-27 Make the server decide where the player should be placed initially
    public static void playerJoinedReceived(ServerProperties serverProperties, float x, float y, float z) {
        ConnectedPlayer connectedPlayer = new ConnectedPlayer(new Vector3f(x, y, z));
        serverProperties.playerJoinedReceived(connectedPlayer);
    }

    public static void playerMovedReceived(ConnectedPlayer player, float x, float y, float z) {
        player.setPosition(x, y, z);
    }

}
