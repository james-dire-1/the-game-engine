package com.james.serverSide.simulation.objects;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a player object on the server-side.
 */
// TODO: 2024-06-30 Make this extend PhysicalObject or something
public class ConnectedPlayer {

    private final Vector3f position;
    public Vector3f getPosition() { return position; }

    public ConnectedPlayer(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

}
