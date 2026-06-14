package com.james.audio.objects;

import com.james.tools.Time;
import com.james.tools.Vector3fInterpolator;
import org.lwjgl.util.vector.Vector3f;

public class SoundEmitter {

    public float lastTime = Time.getCurrentTime();

    private final Vector3f prevPosition;
    private final Vector3f currentPosition;
    private final Vector3f interpolatedPosition;

    public Vector3f getPrevPosition() { return prevPosition; }
    public Vector3f getCurrentPosition() { return currentPosition; }
    public Vector3f getInterpolatedPosition() { return interpolatedPosition; }

    public SoundEmitter(Vector3f position) {
        this.prevPosition = new Vector3f(position);
        this.currentPosition = new Vector3f(position);
        this.interpolatedPosition = new Vector3f(position);
    }

    public void updatePrevPosition() {
        this.prevPosition.set(this.currentPosition);
    }

    public void setCurrentPosition(float x, float y, float z) {
        this.currentPosition.x = x;
        this.currentPosition.y = y;
        this.currentPosition.z = z;
    }

    public void updateInterpolatedPosition() {
        Vector3fInterpolator.interpolate(prevPosition, currentPosition, interpolatedPosition, lastTime, Time.getCurrentTime());
    }

}
