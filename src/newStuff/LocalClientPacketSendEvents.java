package newStuff;

public class LocalClientPacketSendEvents implements ClientPacketSendEvents {

    @Override
    public void sendPlayerJoined(ServerProperties properties, float x, float y, float z) {
        ServerThreadManager.executeOnSimulationThread(() -> {
            ServerPacketReceiveActions.playerJoinedReceived(properties, x, y, z);
        });
    }

    @Override
    public void sendPlayerMoved(ConnectedPlayer player, float x, float y, float z) {
        ServerThreadManager.executeOnSimulationThread(() -> {
            ServerPacketReceiveActions.playerMovedReceived(player, x, y, z);
        });
    }

}
