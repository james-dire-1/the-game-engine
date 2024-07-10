package templates.communication;

import com.james.serverSide.PlayerInfo;
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

    public static void playerJoinedReceived(float x, float y, float z) {
        LocalServerProperties properties = new LocalServerProperties();

        ServerThreadManager.executeOnALevelThread(Level.getByName("main"), () -> {
            ServerPacketReceiveActions.playerJoinedReceived(properties, x, y, z);
        });
    }

    public static void playerMovedReceived(float x, float y, float z) {
        ServerThreadManager.executeOnALevelThread(playerInfo.level, () -> {
            ServerPacketReceiveActions.playerMovedReceived(playerInfo, x, y, z);
        });
    }

    public static void changePauseStateReceived(boolean shouldPause) {
        ServerThreadManager.executeOnALevelThread(playerInfo.level, () -> {
            ServerPacketReceiveActions.changePauseStateReceived(playerInfo, shouldPause);
        });
    }

}
