package com.james.simulation;

import com.james.common.simulation.LevelProperties;
import com.james.simulation.collisionEngine.ClientCollisionHandler;
import com.james.simulation.collisionEngine.RSTClientCollisionHandler;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.simulation.collisionEngine.hitboxes.CachedSphereHitbox;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.Player;
import com.james.simulation.objects.CachedConnectedPlayer;
import com.james.common.simulation.details.FallingAndGravityState;
import templates.gameplay.ClientSideUpdaters;
import templates.communication.ClientPacketSendEvents;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

/**
 * Client version of the Level class. Holds information about cached objects, as well as the Player. Also,
 * since this is a client-side level, there should only be one instance actively being used at any one time,
 * which is why this class is implemented as a singleton.
 */
public class ClientLevel extends LevelProperties {

    public final ClientPacketSendEvents events;
    public boolean isReady = false;

    private final Player player;
    public Player getPlayer() { return player; }

    private final Map<Integer, CachedPhysicalObject> cachedPhysicalObjects = new HashMap<>();
    private final Map<Integer, CachedConnectedPlayer> cachedConnectedPlayers = new HashMap<>();
    private final ClientCollisionHandler clientCollisionHandler;
    private final RSTClientCollisionHandler rstClientCollisionHandler;

    public ClientLevel(ClientPacketSendEvents events, Vector3f playerPosition, Vector3f playerEllipsoidHitboxRadius, float playerSphereHitboxRadius) {
        this.events = events;

        this.player = new Player(this, playerPosition);
        this.clientCollisionHandler = new ClientCollisionHandler(this.player, playerEllipsoidHitboxRadius);
        this.rstClientCollisionHandler = new RSTClientCollisionHandler(this.player, playerSphereHitboxRadius);

        instance = this;
    }

    /**
     * Method that only gets called once the server is ready, i.e. the isReady property is set to true. Gets
     * called every game tick. Collisions are updated here. Called from the PlayerHandler.
     */
    public void update() {
        player.moveUpdate();

        if (!player.canCollideWithTriangles) {
            player.setPositionBasedOnVelocity();
        }

        FallingAndGravityState fgState = player.getFallingAndGravityState();
        if (fgState != null) {
            fgState.update(this);
        }

        clientCollisionHandler.update();
        rstClientCollisionHandler.updateSphereHitboxes();

        ClientSideUpdaters.clientLevelUpdate(clientCollisionHandler, rstClientCollisionHandler);
    }

    public boolean addCachedPhysicalObject(int id, CachedPhysicalObject obj) {
        if (cachedPhysicalObjects.containsKey(id)) {
            return false;
        }

        cachedPhysicalObjects.put(id, obj);
        return true;
    }

    public boolean removeCachedPhysicalObject(int id) {
        CachedPhysicalObject previousValue = cachedPhysicalObjects.remove(id);
        return previousValue != null;
    }

    public CachedPhysicalObject getCachedPhysicalObject(int id) {
        return cachedPhysicalObjects.get(id);
    }

    public Collection<CachedPhysicalObject> getCachedPhysicalObjects() {
        return cachedPhysicalObjects.values();
    }

    public boolean addCachedConnectedPlayer(int id, CachedConnectedPlayer connectedPlayer) {
        if (cachedConnectedPlayers.containsKey(id)) {
            return false;
        }

        cachedConnectedPlayers.put(id, connectedPlayer);
        return true;
    }

    public boolean removeCachedConnectedPlayer(int id) {
        CachedConnectedPlayer previousValue = cachedConnectedPlayers.remove(id);
        return previousValue != null;
    }

    public CachedConnectedPlayer getCachedConnectedPlayer(int id) {
        return cachedConnectedPlayers.get(id);
    }

    public Collection<CachedConnectedPlayer> getCachedConnectedPlayers() {
        return cachedConnectedPlayers.values();
    }

    public boolean addCachedAABBHitbox(int physicalObjectId, CachedAABBHitbox cachedAABBHitbox) {
        CachedAABBHitbox.Identifier identifier = new CachedAABBHitbox.Identifier(physicalObjectId, cachedAABBHitbox.meshPath, cachedAABBHitbox.subMeshIdentifier);

        if (clientCollisionHandler.cachedAABBHitboxes.containsKey(identifier)) {
            return false;
        }

        clientCollisionHandler.cachedAABBHitboxes.put(identifier, cachedAABBHitbox);
        return true;
    }

    public boolean removeCachedAABBHitbox(int physicalObjectId, String meshPath, int subMeshIdentifier) {
        CachedAABBHitbox.Identifier identifier = new CachedAABBHitbox.Identifier(physicalObjectId, meshPath, subMeshIdentifier);
        CachedAABBHitbox previousValue = clientCollisionHandler.cachedAABBHitboxes.remove(identifier);
        return previousValue != null;
    }

    public boolean addCachedSphereHitbox(int physicalObjectId, CachedSphereHitbox cachedSphereHitbox) {
        CachedSphereHitbox.Identifier identifier = new CachedSphereHitbox.Identifier(physicalObjectId, cachedSphereHitbox.radius);

        if (rstClientCollisionHandler.cachedSphereHitboxes.containsKey(identifier)) {
            return false;
        }

        rstClientCollisionHandler.cachedSphereHitboxes.put(identifier, cachedSphereHitbox);
        return true;
    }

    public boolean removeCachedSphereHitbox(int physicalObjectId, float radius) {
        CachedSphereHitbox.Identifier identifier = new CachedSphereHitbox.Identifier(physicalObjectId, radius);
        CachedSphereHitbox previousValue = rstClientCollisionHandler.cachedSphereHitboxes.remove(identifier);
        return previousValue != null;
    }

    private static ClientLevel instance;
    public static ClientLevel get() { return instance; }
    public static void delete() { instance = null; }

}
