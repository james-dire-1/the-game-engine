package templates.communication;

import templates.common.simulation.objects.PhysicalObjectType;
import com.james.renderEngine.ui.Screen;
import com.james.serverSide.LevelInitializer;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.CachedConnectedPlayer;
import com.james.tools.ColorUtils;
import com.james.tools.Time;
import game.main.Main;
import game.ui.screens.ChatScreen;
import newStuff.rayStuffOnHold.GeneralSphereHitbox;
import game.ui.screens.UsernamePromptScreen;
import templates.rendering.PhysicalToVisualConverter;
import templates.gameplay.GameLoader;
import templates.gameplay.LocalGameLoader;
import templates.gameplay.OnlineGameLoader;
import templates.gameplay.PlayerHandler;
import templates.rendering.ModelBank;
import com.james.simulation.ClientLevel;
import org.lwjgl.util.vector.Vector3f;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;

/**
 * Methods that handle what should happen on the client side when particular events occur on the server side.
 */
public class ClientPacketReceiveActions {

    public static void usernamePromptReceived() {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.usernamePromptReceived");

        if (LevelInitializer.isOnlineGame) {
            Screen.queueScreenForAddition(new UsernamePromptScreen());
        } else {
            new LocalClientPacketSendEvents().sendPlayerUsername("localplayer");
        }
    }

    public static void usernameSuccessReceived(String username, int color, Vector3f spawnPoint) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.usernameSuccessReceived");

        PlayerHandler.localUsername = username;
        PlayerHandler.localColor = ColorUtils.asNormalizedRGBArray(color);

        if (LevelInitializer.isOnlineGame) {
            Main.gameLoader = new OnlineGameLoader(spawnPoint);
        } else {
            Main.gameLoader = new LocalGameLoader(spawnPoint);
        }
    }

    public static void levelIsReadyReceived() {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelIsReadyReceived");

        ClientLevel.get().isReady = true;
    }

    public static void physicalObjectAddedReceived(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectAddedReceived " + "{id=" + id + "} {type=" + type +"}");

        CachedPhysicalObject object = new CachedPhysicalObject(new Vector3f(position), new Vector3f(rotation), scale);
        boolean alreadyExists = ClientLevel.get().addCachedPhysicalObject(id, object);
        if (alreadyExists) {
            Warnings.warn("PhysicalObject of id " + id + " and of type " + type + " already exists client-side");
        }

        Model[] modelList = PhysicalToVisualConverter.convert(type);

        for (Model model : modelList) {
            GameObject gameObject = new GameObject(model, new Vector3f(object.getPosition()), new Vector3f(object.getRotation()), scale);
            GameLoader.batchedGameObjectsList.addGameObject(gameObject);

            object.setGameObject(gameObject);
        }
    }

    // TODO: 2024-07-21 There seems to be a lot of code duplication here..
    public static void physicalObjectMovedReceived(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectMovedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.updatePrevPosition();
                object.setPosition(x, y, z);
                object.lastTime = Time.getCurrentTime();
            } else {
                Warnings.warn("Attempting to move a PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
            }
        } else {
            Warnings.warn("Attempting to move a PhysicalObject client-side by id, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

    public static void physicalObjectRotatedReceived(int id, float rotX, float rotY, float rotZ) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectRotatedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.updatePrevRotation();
                object.setRotation(rotX, rotY, rotZ);
                object.lastTime = Time.getCurrentTime();
            } else {
                Warnings.warn("Attempting to rotate a PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
            }
        } else {
            Warnings.warn("Attempting to rotate a PhysicalObject client-side by id, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

    public static void physicalObjectScaledReceived(int id, float scale) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectScaledReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.setScale(scale);
                // This must be done manually since scale is a value type, not a reference type
                object.getGameObject().setScale(scale);
            } else {
                Warnings.warn("Attempting to scale a PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
            }
        } else {
            Warnings.warn("Attempting to scale a PhysicalObject client-side by id, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

    public static void physicalObjectTransformChangedReceived(int id, Vector3f position, Vector3f rotation, float scale) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectTransformChangedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.updatePrevPosition();
                object.updatePrevRotation();
                object.setPosition(position.x, position.y, position.z);
                object.setRotation(rotation.x, rotation.y, rotation.z);
                object.setScale(scale);
                // This must be done manually since scale is a value type, not a reference type
                object.getGameObject().setScale(scale);
                object.lastTime = Time.getCurrentTime();
            } else {
                Warnings.warn("Attempting to transform a PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
            }
        } else {
            Warnings.warn("Attempting to transform a PhysicalObject client-side by id, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

    public static void aabbHitboxAddedReceived(int id, String meshPath, int subMeshIdentifier) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.aabbHitboxAddedReceived " + "{id=" + id + "}");

        ClientLevel.get().addCachedAABBHitbox(new CachedAABBHitbox(id, meshPath, subMeshIdentifier));
    }

    public static void connectedPlayerAddedReceived(int id, String username, int color, float x, float y, float z, float rotY) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.connectedPlayerAddedReceived " + "{id=" + id + "}");

        CachedConnectedPlayer cachedConnectedPlayer = new CachedConnectedPlayer(username, color, new Vector3f(x, y, z), new Vector3f(0, rotY, 0));
        boolean alreadyExists = ClientLevel.get().addCachedConnectedPlayer(id, cachedConnectedPlayer);
        if (alreadyExists) {
            Warnings.warn("ConnectedPlayer of id " + id + " already exists client-side");
        }

        Model model = ModelBank.getAbstractArt();
        GameObject gameObject = new GameObject(model, new Vector3f(cachedConnectedPlayer.getPosition()), new Vector3f(cachedConnectedPlayer.getRotation()), 1);
        GameLoader.batchedGameObjectsList.addGameObject(gameObject);

        cachedConnectedPlayer.setGameObject(gameObject);

        GeneralSphereHitbox generalSphereHitbox = new GeneralSphereHitbox(id, 1);
        ClientLevel.get().addGeneralSphereHitbox(generalSphereHitbox);
    }

    // TODO: 2025-07-01 Make a method that separates transform and rotation perhaps
    public static void connectedPlayerTransformChangedReceived(int id, float x, float y, float z, float rotY) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.connectedPlayerMovedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedConnectedPlayer cachedConnectedPlayer = level.getCachedConnectedPlayer(id);
            if (cachedConnectedPlayer != null) {
                cachedConnectedPlayer.updatePrevPosition();
                cachedConnectedPlayer.updatePrevRotation();
                cachedConnectedPlayer.setPosition(x, y, z);
                cachedConnectedPlayer.setRotation(0, rotY, 0);
                cachedConnectedPlayer.lastTime = Time.getCurrentTime();
            } else {
                Warnings.warn("Attempting to transform a ConnectedPlayer client-side by id, but that ConnectedPlayer doesn't exist client-side");
            }
        } else {
            Warnings.warn("Attempting to transform a ConnectedPlayer client-side by id, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

    public static void connectedPlayerLeftReceived(int id) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.connectedPlayerLeftReceived");

        CachedConnectedPlayer cachedConnectedPlayer = ClientLevel.get().removeCachedConnectedPlayer(id);
        GameLoader.batchedGameObjectsList.removeGameObject(cachedConnectedPlayer.getGameObject());
    }

    public static void levelSecondsPerGameTickChangedReceived(float secondsPerGameTick) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelSecondsPerGameTickChangedReceived");

        ClientLevel.get().secondsPerGameTick = secondsPerGameTick;
    }

    public static void levelGravityChangedReceived(float x, float y, float z) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelGravityChangedReceived");

        ClientLevel.get().gravity.set(x, y, z);
    }

    public static void chatMessageReceptionConfirmationReceived(int localMessageId) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.chatMessageReceptionConfirmationReceived");

        // TODO: 2025-07-25  
    }

    public static void chatMessageReceived(int playerId, String message) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.chatMessageReceived");

        ClientLevel level = ClientLevel.get();
        ChatScreen chatScreen = ChatScreen.get();

        if (level != null) {
            if (chatScreen != null) {
                CachedConnectedPlayer cachedConnectedPlayer = level.getCachedConnectedPlayer(playerId);
                String username = cachedConnectedPlayer.username;
                float[] color = cachedConnectedPlayer.color;
                chatScreen.appendChatWithPlayerMessage(username, color, message);
            } else {
                Warnings.warn("Attempting to receive a chat message, but the client's ChatScreen object hasn't even been instantiated yet");
            }
        } else {
            Warnings.warn("Attempting to receive a chat message, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

    public static void systemMessageReceived(String message) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.systemMessageReceived");

        ClientLevel level = ClientLevel.get();
        ChatScreen chatScreen = ChatScreen.get();

        if (level != null) {
            if (chatScreen != null) {
                chatScreen.appendChatWithSystemMessage(message);
            } else {
                Warnings.warn("Attempting to receive a system message, but the client's ChatScreen object hasn't even been instantiated yet");
            }
        } else {
            Warnings.warn("Attempting to receive a system message, but the client's ClientLevel object hasn't even been instantiated yet");
        }
    }

}
