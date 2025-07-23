package templates.gameplay;

import com.james.common.simulation.objects.PhysicalObjectType;
import com.james.networking.Client;
import com.james.renderEngine.ui.UiHandler;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.simulation.ClientLevel;
import com.james.tools.ThreadManager;
import game.main.Main;
import game.ui.screens.DisconnectedScreen;
import templates.settings.GLFWWindowTitles;
import templates.communication.OnlineClientPacketSendEvents;
import org.lwjgl.util.vector.Vector3f;
import templates.communication.ClientPacketReceiveActions;

public class OnlineGameLoader extends GameLoader {

    public OnlineGameLoader() {
        super(OnlineClientPacketSendEvents.get());

        GLFWUtilities.setWindowTitle(GLFWWindowTitles.MULTIPLAYER);
    }

    @Override
    protected void additionalStartupActions() {
    }

    @Override
    protected void onGameClientClosing() {
        Client.get().disconnect();
    }

    public static void usernamePromptReceived(Object[] objects) {
        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::usernamePromptReceived);
    }

    public static void usernameSuccessReceived(Object[] objects) {
        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::usernameSuccessReceived);
    }

    public static void levelIsReadyReceived(Object[] objects) {
        ThreadManager.executeOnMainThread(ClientPacketReceiveActions::levelIsReadyReceived);
    }

    public static void physicalObjectAddedReceived(Object[] objects) {
        int id = (int) objects[0];
        PhysicalObjectType type = (PhysicalObjectType) objects[1];
        Vector3f position = new Vector3f((float) objects[2], (float) objects[3], (float) objects[4]);
        Vector3f rotation = new Vector3f((float) objects[5], (float) objects[6], (float) objects[7]);
        float scale = (float) objects[8];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectAddedReceived(id, type, position, rotation, scale);
        });
    }

    public static void physicalObjectMovedReceived(Object[] objects) {
        int id = (int) objects[0];
        float x = (float) objects[1];
        float y = (float) objects[2];
        float z = (float) objects[3];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectMovedReceived(id, x, y, z);
        });
    }

    public static void physicalObjectRotatedReceived(Object[] objects) {
        int id = (int) objects[0];
        float rotX = (float) objects[1];
        float rotY = (float) objects[2];
        float rotZ = (float) objects[3];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectRotatedReceived(id, rotX, rotY, rotZ);
        });
    }

    public static void physicalObjectScaledReceived(Object[] objects) {
        int id = (int) objects[0];
        float scale = (float) objects[1];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectScaledReceived(id, scale);
        });
    }

    public static void physicalObjectTransformChangedReceived(Object[] objects) {
        int id = (int) objects[0];
        Vector3f position = new Vector3f((float) objects[1], (float) objects[2], (float) objects[3]);
        Vector3f rotation = new Vector3f((float) objects[4], (float) objects[5], (float) objects[6]);
        float scale = (float) objects[7];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.physicalObjectTransformChangedReceived(id, position, rotation, scale);
        });
    }

    public static void aabbHitboxAddedReceived(Object[] objects) {
        int id = (int) objects[0];
        String meshPath = (String) objects[1];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.aabbHitboxAddedReceived(id, meshPath);
        });
    }

    public static void connectedPlayerAddedReceived(Object[] objects) {
        int id = (int) objects[0];
        float x = (float) objects[1];
        float y = (float) objects[2];
        float z = (float) objects[3];
        float rotY = (float) objects[4];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.connectedPlayerAddedReceived(id, x, y, z, rotY);
        });
    }

    public static void connectedPlayerTransformChangedReceived(Object[] objects) {
        int id = (int) objects[0];
        float x = (float) objects[1];
        float y = (float) objects[2];
        float z = (float) objects[3];
        float rotY = (float) objects[4];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.connectedPlayerTransformChangedReceived(id, x, y, z, rotY);
        });
    }

    public static void connectedPlayerLeftReceived(Object[] objects) {
        int id = (int) objects[0];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.connectedPlayerLeftReceived(id);
        });
    }

    public static void levelSecondsPerGameTickChangedReceived(Object[] objects) {
        float secondsPerGameTick = (float) objects[0];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.levelSecondsPerGameTickChangedReceived(secondsPerGameTick);
        });
    }

    public static void levelGravityChangedReceived(Object[] objects) {
        float x = (float) objects[0];
        float y = (float) objects[1];
        float z = (float) objects[2];

        ThreadManager.executeOnMainThread(() -> {
            ClientPacketReceiveActions.levelGravityChangedReceived(x, y, z);
        });
    }

    public static void onConnectionException(Exception e, Client client) {
        ThreadManager.executeOnMainThread(() -> {
            String exceptionMessage = e.getMessage();

             if (exceptionMessage == null || !exceptionMessage.equals("Socket closed")) {
                UiHandler.screens.add(new DisconnectedScreen(e));

                ClientLevel.delete();
                Main.gameLoader = null;
             }
        });
    }

}
