package templates.communication;

import game.main.LocalGameLoader;
import templates.serverSide.communication.ServerPacketReceiveActions;
import com.james.serverSide.ServerThreadManager;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;

/**
 * A class containing methods that dictate what should happen when particular client events occur. In this case
 * specifically, these implemented methods dictate what should happen if the client that is running is a local
 * client (i.e. it is not an online game). In this case, we don't need to send data over a network, but rather
 * we can call the corresponding methods in ServerPacketReceiveActions directly.
 *
 * @implNote These methods get called from the GameLoader or other client-side code.
 * @see ServerPacketReceiveActions
 * @see ServerThreadManager
 * @see LocalGameLoader
 */
public class LocalClientPacketSendEvents implements ClientPacketSendEvents {

    @Override
    public void sendPlayerJoined(float x, float y, float z, float rotY) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalClientPacketSendEvents.sendPlayerJoined");
        LocalServerProperties.playerJoinedReceived(x, y, z, rotY);
    }

    // TODO: 2024-06-29 Implement a way to remove duplicate move packets from the server once it is time to process them
    @Override
    public void sendPlayerTransformChanged(float x, float y, float z, float rotY) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("LocalClientPacketSendEvents.sendPlayerMoved");
        LocalServerProperties.playerTransformChangedReceived(x, y, z, rotY);
    }

    @Override
    public void sendChangePauseState(boolean shouldPause) {
        if (IS_NETWORK_DEBUG) System.out.println("LocalClientPacketSendEvents.sendChangePauseState" + "{shouldPause=" + shouldPause + "}");
        LocalServerProperties.changePauseStateReceived(shouldPause);
    }

}
