package com.james.renderEngine.uiElements.tools;

import com.james.tools.Time;

public class CaratBlinker {

    private static final float BLINK_TIME = 0.5f;

    public boolean blinkState;
    public boolean focused = true;
    public boolean stateChangedThisFrame;

    private float lastBlinkTime = Time.getCurrentTime();
    private boolean justReset;

    public void update() {
        stateChangedThisFrame = false;

        if (justReset) {
            justReset = false;
            stateChangedThisFrame = true;
        }

        if (focused && Time.getCurrentTime() - lastBlinkTime >= BLINK_TIME) {
            lastBlinkTime = Time.getCurrentTime();
            blinkState = !blinkState;
            stateChangedThisFrame = true;
        }
    }

    public void reset() {
        lastBlinkTime = Time.getCurrentTime();
        blinkState = true;
        justReset = true;
    }

}
