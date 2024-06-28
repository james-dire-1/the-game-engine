package newStuff;

import org.lwjgl.util.vector.Vector3f;

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
