package templates.communication;

import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.common.simulation.objects.PhysicalObjectType;
import com.james.simulation.objects.CachedPhysicalObject;
import game.main.LocalGameLoader;
import templates.rendering.ModelBank;
import com.james.simulation.ClientLevel;
import org.lwjgl.util.vector.Vector3f;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;

/**
 * Methods that handle what should happen on the client side when particular events occur on the server side.
 */
public class ClientPacketReceiveActions {

    public static void levelIsReadyReceived() {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelIsReadyReceived");

        ClientLevel.get().isReady = true;
    }

    public static void physicalObjectAddedReceived(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectAddedReceived " + "{id=" + id + "} {type=" + type +"}");

        CachedPhysicalObject object = new CachedPhysicalObject(id, new Vector3f(position), new Vector3f(rotation), scale);
        boolean alreadyExists = ClientLevel.get().addCachedPhysicalObject(id, object);

        if (alreadyExists) {
            Warnings.printClientServerDeSyncWarning("PhysicalObject of id " + id + " and of type " + type + " already exists client-side");
        }

        Model model = null;
        if (type == PhysicalObjectType.Wall) {
            model = ModelBank.getWall();
        } else if (type == PhysicalObjectType.TestEnvironment) {
            model = ModelBank.getTestEnvironment();
        } else if (type == PhysicalObjectType.Other) {
            model = ModelBank.getAbstractArt();
        }

        GameObject gameObject = new GameObject(model, object.getPosition(), object.getRotation(), scale);
        LocalGameLoader.batchedGameObjectsList.addGameObject(gameObject);
    }

    public static void physicalObjectMovedReceived(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectMovedReceived");

        CachedPhysicalObject obj = ClientLevel.get().getCachedPhysicalObject(id);
        if (obj != null) {
            obj.setPosition(x, y, z);
        } else {
            Warnings.printClientServerDeSyncWarning("Attempting to move a PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
        }
    }

    public static void aabbHitboxAddedReceived(int id, String meshPath) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.aabbHitboxAddedReceived " + "{id=" + id + "}");

        ClientLevel.get().addCachedAABBHitbox(new CachedAABBHitbox(id, meshPath));
    }

    public static void levelSecondsPerGameTickChangedReceived(float secondsPerGameTick) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelSecondsPerGameTickChangedReceived");

        ClientLevel.get().secondsPerGameTick = secondsPerGameTick;
    }

    public static void levelGravityChangedReceived(float x, float y, float z) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelGravityChangedReceived");

        ClientLevel.get().gravity.set(x, y, z);
    }

}
