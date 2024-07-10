package templates.serverSide.communication;

import com.james.serverSide.PlayerInfo;
import com.james.serverSide.simulation.Level;
import com.james.serverSide.simulation.collisionEngine.hitboxes.AABBHitbox;
import com.james.serverSide.simulation.objects.ConnectedPlayer;
import com.james.serverSide.simulation.objects.PhysicalObject;
import org.lwjgl.util.vector.Vector3f;
import templates.common.GlobalConstants;

/**
 * Methods that handle what should happen on the server side when particular events occur on the client side.
 */
public class ServerPacketReceiveActions {

    // TODO: 2024-06-27 Make the server decide where the player should be placed initially
    public static void playerJoinedReceived(ServerProperties serverProperties, float x, float y, float z) {
        if (GlobalConstants.IS_NETWORK_DEBUG) System.out.println("ServerPacketReceiveActions.playerJoinedReceived");

        Level startLevel = Level.getByName("main");
        PlayerInfo playerInfo = new PlayerInfo(startLevel);
        ConnectedPlayer connectedPlayer = new ConnectedPlayer(new Vector3f(x, y, z));
        startLevel.addConnectedPlayer(playerInfo, connectedPlayer);

        serverProperties.assignPlayerInfo(playerInfo);

        for (PhysicalObject obj : startLevel.getPhysicalObjects()) {
            startLevel.events.sendPhysicalObjectAddedToLevel(obj.id, obj.type, obj.getPosition(), obj.getRotation(), obj.getScale());
        }
        for (AABBHitbox aabbHitbox : startLevel.getAABBHitboxes()) {
            startLevel.events.sendAABBHitboxAdded(((PhysicalObject) aabbHitbox.object).id, aabbHitbox.meshPath);
        }
        startLevel.events.notifyThatLevelIsReady();
    }

    public static void playerMovedReceived(PlayerInfo playerInfo, float x, float y, float z) {
        if (GlobalConstants.IS_DETAILED_NETWORK_DEBUG) System.out.println("ServerPacketReceiveActions.playerMovedReceived");

        ConnectedPlayer connectedPlayer = playerInfo.getConnectedPlayer();
        connectedPlayer.setPosition(x, y, z);
    }

    public static void changePauseStateReceived(PlayerInfo playerInfo, boolean shouldPause) {
        if (GlobalConstants.IS_NETWORK_DEBUG) System.out.println("ServerPacketReceiveActions.changePauseStateReceived " + "{shouldPause=" + shouldPause + "}");

        playerInfo.level.isPaused = shouldPause;
    }

}
