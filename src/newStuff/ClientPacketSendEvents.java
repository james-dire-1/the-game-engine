package newStuff;

public interface ClientPacketSendEvents {

    // TODO: 2024-06-28 In the future, don't send the position of where the player should spawn server-side
    void sendPlayerJoined(ServerProperties properties, float x, float y, float z);
    // TODO: 2024-06-27 In the future, only send if player was moved, instead of all the time
    void sendPlayerMoved(ConnectedPlayer player, float x, float y, float z);

}
