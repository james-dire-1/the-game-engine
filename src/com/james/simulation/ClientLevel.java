package com.james.simulation;

import com.james.common.simulation.LevelProperties;
import com.james.simulation.collisionEngine.ClientCollisionHandler;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.Player;
import com.james.simulation.objects.CachedConnectedPlayer;
import newStuff.GeneralSphereCollisionHandler;
import newStuff.GeneralSphereHitbox;
import templates.communication.ClientPacketSendEvents;
import org.lwjgl.util.vector.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Client version of the Level class. Holds information about cached objects, as well as the Player.
 * Also, since this is a client-side level, there should only be one of these classes actively being
 * used at any one time, which is why this class is implemented as a singleton.
 */
public class ClientLevel extends LevelProperties {

    public final ClientPacketSendEvents events;
    public boolean isReady;

    private final Player player;
    public Player getPlayer() { return player; }

    private final Map<Integer, CachedPhysicalObject> cachedPhysicalObjects = new HashMap<>();
    private final Map<Integer, CachedConnectedPlayer> cachedConnectedPlayers = new HashMap<>();
    private final ClientCollisionHandler clientCollisionHandler;
    private final GeneralSphereCollisionHandler generalSphereCollisionHandler;

    public ClientLevel(ClientPacketSendEvents events, Vector3f playerPosition, Vector3f playerHitboxRadius) {
        this.events = events;

        this.player = new Player(this, playerPosition);
        this.clientCollisionHandler = new ClientCollisionHandler(this, this.player, playerHitboxRadius);
        this.generalSphereCollisionHandler = new GeneralSphereCollisionHandler();

        instance = this;
    }

    /**
     * Method that only gets called once the server is ready, i.e. the isReady property is set to true.
     * Gets called every game tick. Collisions are updated here. Called from the PlayerHandler.
     */
    public void update() {
        if (!player.isAffectedByAABBCollisions) {
            player.moveUpdate();
        }

        clientCollisionHandler.update();

        // todo
    }

    public boolean addCachedPhysicalObject(int id, CachedPhysicalObject obj) {
        boolean alreadyExists;

        if (!cachedPhysicalObjects.containsKey(id)) {
            alreadyExists = false;
            cachedPhysicalObjects.put(id, obj);
        } else {
            alreadyExists = true;
        }

        return alreadyExists;
    }

    public CachedPhysicalObject getCachedPhysicalObject(int id) {
        return cachedPhysicalObjects.get(id);
    }

    public Collection<CachedPhysicalObject> getCachedPhysicalObjects() {
        return cachedPhysicalObjects.values();
    }

    public void addCachedAABBHitbox(CachedAABBHitbox cachedAABBHitbox) {
        clientCollisionHandler.cachedAABBHitboxes.add(cachedAABBHitbox);
    }

    public boolean addCachedConnectedPlayer(int id, CachedConnectedPlayer connectedPlayer) {
        boolean alreadyExists;

        if (!cachedConnectedPlayers.containsKey(id)) {
            alreadyExists = false;
            cachedConnectedPlayers.put(id, connectedPlayer);
        } else {
            alreadyExists = true;
        }

        return alreadyExists;
    }

    public void addGeneralSphereHitbox(GeneralSphereHitbox generalSphereHitbox) {
        generalSphereCollisionHandler.generalSphereHitboxes.add(generalSphereHitbox);
    }

    public CachedConnectedPlayer removeCachedConnectedPlayer(int id) {
        return cachedConnectedPlayers.remove(id);
    }

    public CachedConnectedPlayer getCachedConnectedPlayer(int id) {
        return cachedConnectedPlayers.get(id);
    }

    public Collection<CachedConnectedPlayer> getCachedConnectedPlayers() {
        return cachedConnectedPlayers.values();
    }

    private static ClientLevel instance;
    public static ClientLevel get() { return instance; }
    public static void delete() { instance = null; }

}
