package templates.communication;

import templates.serverSide.PlayerInfo;
import com.james.serverSide.ServerThreadManager;
import com.james.serverSide.simulation.Level;
import templates.serverSide.communication.ServerPacketReceiveActions;
import templates.serverSide.communication.ServerProperties;

/**
 * Dictates what should happen when a player joins the server for a local game. Technically, the
 * variable playerInfo is also accessed client-side, which is why it is marked as volatile.
 */
// TODO: 2024-06-30 Redo this documentation
public class LocalServerProperties implements ServerProperties {

    public static volatile PlayerInfo playerInfo;

    @Override
    public void assignPlayerInfo(PlayerInfo playerInfo) {
        LocalServerProperties.playerInfo = playerInfo;
    }

    public static void clientJoined() {
        LocalServerProperties serverProperties = new LocalServerProperties();

        ServerThreadManager.executeOnALevelThread(Level.getByName("main"), () -> {
            ServerPacketReceiveActions.clientJoined(serverProperties);
        });
    }

    public static void playerUsernameReceived(String username) {
        ServerThreadManager.executeOnALevelThread(Level.getByName("main"), () -> {
            ServerPacketReceiveActions.playerUsernameReceived(playerInfo, username);
        });
    }

    public static void playerJoinedReceived(float x, float y, float z, float rotY) {
        ServerThreadManager.executeOnALevelThread(Level.getByName("main"), () -> {
            ServerPacketReceiveActions.playerJoinedReceived(playerInfo, x, y, z, rotY);
        });
    }

    public static void playerTransformChangedReceived(float x, float y, float z, float rotY) {
        ServerThreadManager.executeOnALevelThread(playerInfo.level, () -> {
            ServerPacketReceiveActions.playerTransformChangedReceived(playerInfo, x, y, z, rotY);
        });
    }

    // TODO: 2024-07-12 DOES THIS ACTUALLY HAVE TO EVER GET CALLED?
    public static void playerLeftReceived() {
        ServerThreadManager.executeOnALevelThread(playerInfo.level, () -> {
            ServerPacketReceiveActions.playerLeftReceived(playerInfo);
        });
    }

    public static void changePauseStateReceived(boolean shouldPause) {
        ServerThreadManager.executeOnALevelThread(playerInfo.level, () -> {
            ServerPacketReceiveActions.changePauseStateReceived(playerInfo, shouldPause);
        });
    }

    public static void chatMessageReceived(int localMessageId, String message) {
        ServerThreadManager.executeOnALevelThread(playerInfo.level, () -> {
            ServerPacketReceiveActions.chatMessageReceived(playerInfo, localMessageId, message);
        });
    }

}
