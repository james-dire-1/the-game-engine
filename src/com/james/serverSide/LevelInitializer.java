package com.james.serverSide;

import game.serverSide.communication.ServerPacketSendEvents;
import com.james.tools.Time;

/**
 * Class that handles the management of a Level instance. That is, it creates a new Level when constructed,
 * and creates a new Thread for the game loop on which the Level will be run. Calls the Level's update()
 * method every game tick.
 * @see Level
 */
public abstract class LevelInitializer implements Runnable {

    public volatile boolean shouldRun = true;

    protected final Level level;

    public LevelInitializer(ServerPacketSendEvents events) {
        this.level = new Level(events);
        Thread thread = new Thread(this);
        thread.start();
    }

    public abstract void onStartup();

    @Override
    public void run() {
        onStartup();

        float lastTime = Time.getCurrentTime();
        while (shouldRun) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Time.getCurrentTime() - lastTime >= level.secondsPerGameTick) {
                lastTime = Time.getCurrentTime();
                level.update();
            }
        }
    }

}
