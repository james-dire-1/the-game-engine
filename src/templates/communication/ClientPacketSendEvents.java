package templates.communication;

import com.james.serverSide.PlayerInfo;
import templates.serverSide.communication.ServerProperties;

/**
 * Various unimplemented methods for things that should be done when particular events are triggered on
 * the client. Implementation of these methods depends on whether the client is online (i.e.
 * data needs to be sent to the server), or local (i.e. server methods can be called directly).
 */
public interface ClientPacketSendEvents {

    // TODO: 2024-06-28 In the future, don't send the position of where the player should spawn server-side
    void sendPlayerJoined(float x, float y, float z);
    // TODO: 2024-06-27 In the future, only send if player was moved, instead of all the time
    void sendPlayerMoved(float x, float y, float z);
    void sendChangePauseState(boolean shouldPause);

}
