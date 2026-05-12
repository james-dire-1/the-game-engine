package com.james.simulation.objects;

import com.james.common.simulation.LevelProperties;
import com.james.serverSide.simulation.objects.MovableObject;
import templates.common.simulation.objects.PhysicalObjectType;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

public class Player extends MovableObject {

    public float lastTime = Time.getCurrentTime();

    private final Vector3f prevPosition;
    public Vector3f getPrevPosition() { return prevPosition; }

    public Player(LevelProperties levelProperties, Vector3f position) {
        super(levelProperties, PhysicalObjectType.Other, position, new Vector3f(), 1);
        this.prevPosition = new Vector3f(position);
    }

    public void updatePrevPosition() {
        this.prevPosition.set(this.position);
    }

}
