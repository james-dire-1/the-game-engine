package templates.communication;

import com.james.common.networking.Packet;
import templates.common.networking.PacketType;
import com.james.networking.Client;
import templates.communication.ClientPacketSendEvents;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;

public class OnlineClientPacketSendEvents implements ClientPacketSendEvents {

    @Override
    public void sendPlayerJoined(float x, float y, float z, float rotY) {
        if (IS_NETWORK_DEBUG) System.out.println("OnlineClientPacketSendEvents.sendPlayerJoined");

        Object[] objects = { x, y, z, rotY };

        Packet packet = new Packet(PacketType.PLAYER_JOINED, objects);
        Client.get().sendPacket(packet);
    }

    @Override
    public void sendPlayerTransformChanged(float x, float y, float z, float rotY) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("OnlineClientPacketSendEvents.sendPlayerMoved");

        Object[] objects = { x, y, z, rotY };

        Packet packet = new Packet(PacketType.PLAYER_MOVED, objects);
        Client.get().sendPacket(packet);
    }

    @Override
    public void sendChangePauseState(boolean shouldPause) {
        if (IS_NETWORK_DEBUG) System.out.println("OnlineClientPacketSendEvents.sendChangePauseState" + "{shouldPause=" + shouldPause + "}");

        Object[] objects = { shouldPause };

        Packet packet = new Packet(PacketType.LEVEL_CHANGE_PAUSE_STATE, objects);
        Client.get().sendPacket(packet);
    }

}
