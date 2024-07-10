package com.james.simulation.objects;

import com.james.common.simulation.LevelProperties;
import com.james.serverSide.simulation.objects.MovableObject;
import com.james.common.simulation.objects.PhysicalObjectType;
import org.lwjgl.util.vector.Vector3f;

public class Player extends MovableObject {

    public Player(LevelProperties levelProperties, Vector3f position) {
        super(levelProperties, PhysicalObjectType.Other, position, new Vector3f(), 1);
    }

}
