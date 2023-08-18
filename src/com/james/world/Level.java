package com.james.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Level {

    public float secondsPerGameTick = 0.05f;

    public final CollisionHandler collisionHandler = new CollisionHandler(this);
    private final List<PhysicalObject> physicalObjects = new ArrayList<>();

    public final ServerPacketSendEvents events;

    public Level(ServerPacketSendEvents events) {
        this.events = events;
    }

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
