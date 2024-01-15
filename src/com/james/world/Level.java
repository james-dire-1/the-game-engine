package com.james.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class representing an instance of a game world, sort of what some game engines would call a "Scene".
 * Each instance of Level contains a list of PhysicalObjects belonging to it, as well as an update method
 * which gets called from its LevelInitializer every game tick (determined by the Level).
 * @see LevelInitializer
 * @see PhysicalObject
 */
public class Level {

    public float secondsPerGameTick = 0.05f;

    public final CollisionHandler collisionHandler = new CollisionHandler(this);
    private final List<PhysicalObject> physicalObjects = new ArrayList<>();

    public final ServerPacketSendEvents events;

    public Level(ServerPacketSendEvents events) {
        this.events = events;
    }

    /**
     * Method that gets called every game tick, calling all the Level's PhysicalObjects' update() method.
     * It also updates collisions.
     */
    public void update() {
        Iterator<PhysicalObject> iterator = physicalObjects.iterator();
        while (iterator.hasNext()) {
            PhysicalObject obj = iterator.next();

            boolean shouldDelete = obj.update();
            if (shouldDelete) {
                iterator.remove();
            }
        }

        collisionHandler.update();

        physicalObjects.addAll(objectsToAdd);
        objectsToAdd.clear();
    }

    private final List<PhysicalObject> objectsToAdd = new ArrayList<>();
    public void add(PhysicalObject obj) {
        objectsToAdd.add(obj);
    }

}
