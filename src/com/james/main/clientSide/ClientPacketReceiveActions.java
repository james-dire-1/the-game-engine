package com.james.main.clientSide;

import com.james.main.*;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.world.PhysicalObjectType;
import com.james.world.WallTriangle;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

/**
 * Methods that handle what should happen on the client side when particular events occur on the server side.
 * This class also contains important variables, and cached instances of classes from the server; it contains
 * a list of the CachedObjectHitboxes, and a list of the cached WallTriangles.
 */
public class ClientPacketReceiveActions {

    public static Player player;
    public static PlayerHitbox playerHitbox;
    public static final float secondsTillEndCollisionForPlayer = 1;
    public static final Map<Integer, CachedPhysicalObject> cachedLocalPhysicalObjects = new HashMap<>();
    public static final List<CachedObjectHitbox> cachedLocalObjectHitboxes = new ArrayList<>();
    public static final List<WallTriangle> cachedLocalWallTriangles = new ArrayList<>();

    public static void physicalObjectAddedReceived(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        CachedPhysicalObject object = new CachedPhysicalObject(id, new Vector3f(position), new Vector3f(rotation), scale);
        cachedLocalPhysicalObjects.put(id, object);

        Model model = null;
        if (type == PhysicalObjectType.Wall) {
            model = ModelBank.getWall();
        } else if (type == PhysicalObjectType.Other) {
            model = ModelBank.getAbstractArt();
        }

        GameObject gameObject = new GameObject(model, object.getPosition(), object.getRotation(), scale);
        Main.batchedGameObjectsList.addGameObject(gameObject);
    }

    public static void physicalObjectMovedReceived(int id, float x, float y, float z) {
        CachedPhysicalObject obj = cachedLocalPhysicalObjects.get(id);
        if (obj != null) {
            obj.setPosition(x, y, z);
        } else {
            Warnings.printClientServerDeSyncWarning("Attempting to move an PhysicalObject client-side by id, but that PhysicalObject doesn't exist client-side");
        }
    }

    public static void objectHitboxAddedReceived(int idOfCorrespondingObject, float radius) {
        cachedLocalObjectHitboxes.add(new CachedObjectHitbox(idOfCorrespondingObject, radius));
    }

    public static void wallTrianglesAddedReceived(WallTriangle[] wallTriangles) {
        for (WallTriangle wallTriangle : wallTriangles) {
            cachedLocalWallTriangles.add(new WallTriangle(wallTriangle));
        }
    }


}
