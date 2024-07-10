package newStuff;

import com.james.common.networking.PacketType;
import com.james.common.simulation.objects.PhysicalObjectType;
import com.james.networking.Client;
import com.james.tools.ThreadManager;
import org.lwjgl.util.vector.Vector3f;
import templates.communication.ClientPacketReceiveActions;

public class OnlineGameLoader extends GameLoader {

    public OnlineGameLoader() {
        super(new OnlineClientPacketSendEvents());

        Client client = Client.get();

        client.setReadListener(PacketType.LEVEL_IS_READY, OnlineGameLoader::levelIsReadyReceived);
        client.setReadListener(PacketType.PHYSICAL_OBJECT_ADDED_TO_LEVEL, OnlineGameLoader::physicalObjectAddedReceived);
        client.setReadListener(PacketType.PHYSICAL_OBJECT_MOVED, OnlineGameLoader::physicalObjectMovedReceived);
        client.setReadListener(PacketType.AABB_HITBOX_ADDED, OnlineGameLoader::aabbHitboxAddedReceived);
        client.setReadListener(PacketType.LEVEL_SECONDS_PER_GAME_TICK_CHANGED, OnlineGameLoader::levelSecondsPerGameTickChangedReceived);
        client.setReadListener(PacketType.LEVEL_GRAVITY_CHANGED, OnlineGameLoader::levelGravityChangedReceived);
    }

    @Override
    protected void additionalStartupActions() {
    }

    @Override
    protected void onGameClientClosing() {
        Client.get().disconnect();
    }

    private static void levelIsReadyReceived(Object[] objects) {
        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::levelIsReadyReceived);
    }

    private static void physicalObjectAddedReceived(Object[] objects) {
        int id = (int) objects[0];
        PhysicalObjectType type = (PhysicalObjectType) objects[1];
        Vector3f position = new Vector3f((float) objects[2], (float) objects[3], (float) objects[4]);
        Vector3f rotation = new Vector3f((float) objects[5], (float) objects[6], (float) objects[7]);
        float scale = (float) objects[8];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectAddedReceived(id, type, position, rotation, scale);
        });
    }

    private static void physicalObjectMovedReceived(Object[] objects) {
        int id = (int) objects[0];
        float x = (float) objects[1];
        float y = (float) objects[2];
        float z = (float) objects[3];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectMovedReceived(id, x, y, z);
        });
    }

    private static void aabbHitboxAddedReceived(Object[] objects) {
        int id = (int) objects[0];
        String meshPath = (String) objects[1];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.aabbHitboxAddedReceived(id, meshPath);
        });
    }

    private static void levelSecondsPerGameTickChangedReceived(Object[] objects) {
        float secondsPerGameTick = (float) objects[0];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.levelSecondsPerGameTickChangedReceived(secondsPerGameTick);
        });
    }

    private static void levelGravityChangedReceived(Object[] objects) {
        float x = (float) objects[0];
        float y = (float) objects[1];
        float z = (float) objects[2];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.levelGravityChangedReceived(x, y, z);
        });
    }

}
