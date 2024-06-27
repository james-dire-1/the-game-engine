package game.communication;

import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.common.simulation.LevelProperties;
import com.james.simulation.collisionEngine.hitboxes.PlayerHitbox;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.common.simulation.objects.PhysicalObjectType;
import com.james.simulation.objects.CachedPhysicalObject;
import game.main.GameLoader;
import game.main.Main;
import game.rendering.ModelBank;
import game.player.Player;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

/**
 * Methods that handle what should happen on the client side when particular events occur on the server side.
 * This class also contains important variables, and cached instances of classes from the server; it contains
 * a list of the CachedObjectHitboxes, and a list of the cached WallTriangles.
 */
// TODO: 2024-06-24 This class needs to be divided up in the future
public class ClientPacketReceiveActions {

    public static Player player;
    public static PlayerHitbox playerHitbox;
    public static final Map<Integer, CachedPhysicalObject> cachedLocalPhysicalObjects = new HashMap<>();
    public static final List<CachedAABBHitbox> cachedLocalAABBHitboxes = new ArrayList<>();
    public static LevelProperties levelProperties = new LevelProperties();

    public static void serverIsReadyReceived() {
        GameLoader.serverIsReady = true;
    }

    public static void physicalObjectAddedReceived(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        CachedPhysicalObject object = new CachedPhysicalObject(id, new Vector3f(position), new Vector3f(rotation), scale);
        cachedLocalPhysicalObjects.put(id, object);

        Model model = null;
        if (type == PhysicalObjectType.Wall) {
            model = ModelBank.getWall();
        } else if (type == PhysicalObjectType.TestEnvironment) {
            model = ModelBank.getTestEnvironment();
        } else if (type == PhysicalObjectType.Other) {
            model = ModelBank.getAbstractArt();
        }

        GameObject gameObject = new GameObject(model, object.getPosition(), object.getRotation(), scale);
        GameLoader.batchedGameObjectsList.addGameObject(gameObject);
    }

    public static void physicalObjectMovedReceived(int id, float x, float y, float z) {
        CachedPhysicalObject obj = cachedLocalPhysicalObjects.get(id);
        if (obj != null) {
            obj.setPosition(x, y, z);
        } else {
            Warnings.printClientServerDeSyncWarning("Attempting to move a PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
        }
    }

    public static void aabbHitboxAddedReceived(int id, String meshPath) {
        cachedLocalAABBHitboxes.add(new CachedAABBHitbox(id, meshPath));
    }

    public static void levelSecondsPerGameTickChangedReceived(float secondsPerGameTick) {
        levelProperties.secondsPerGameTick = secondsPerGameTick;
    }

    public static void levelGravityChangedReceived(float x, float y, float z) {
        levelProperties.gravity.set(x, y, z);
    }

}
