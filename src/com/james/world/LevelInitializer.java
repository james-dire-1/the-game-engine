package com.james.world;

import com.james.evenNewerCollisionsStuff.EllipsoidDimensions;
import com.james.evenNewerCollisionsStuff.ModelMeshBankInR3;
import com.james.evenNewerCollisionsStuff.ModelPreparations;
import com.james.tools.ModelLoader;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;

/**
 * Class that handles the management of a Level instance. That is, it creates a new Level when constructed,
 * and creates a new Thread for the game loop on which the Level will be run. Calls the Level's update()
 * method every game tick.
 * @implNote This class should be designed in such a way that the standalone server software would not need
 * to rely on it. However, both the standalone server software and the actual game client will use the same
 * Level class.
 * @see Level
 */
public class LevelInitializer implements Runnable {

    public volatile boolean shouldRun = true;

    private final Level level;

    public LevelInitializer(ServerPacketSendEvents events) {
        this.level = new Level(events);
        Thread thread = new Thread(this);
        thread.start();
    }

    /* @Override
    public void run() {
        // TODO: 2024-06-16 Put this method call somewhere in main
        ModelPreparations.init();

        // Do some stuff hoss
    } */

    @Override
    public void run() {
        MovableObject movableObject = new MovableObject(level, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
        level.add(movableObject);
        level.events.sendPhysicalObjectAddedToLevel(movableObject.id, movableObject.type, movableObject.getPosition(), movableObject.getRotation(), movableObject.getScale());

        ModelLoader wallModelLoader = new ModelLoader("res/one-sided-wall5.dae");
        TriangleMesh triangleMesh = new TriangleMesh(wallModelLoader.vertexPositions(), wallModelLoader.indices());

        for (int i = 0; i < 10; i++) {
            PhysicalObject wall = new PhysicalObject(PhysicalObjectType.Wall, new Vector3f(-i*2, 0, -10),
                    new Vector3f(0, 0, 0), 1);
            level.add(wall);
            level.events.sendPhysicalObjectAddedToLevel(wall.id, wall.type, wall.getPosition(), wall.getRotation(), wall.getScale());
            MeshHitbox wallHitbox = new MeshHitbox(wall, triangleMesh);
            level.collisionHandler.walls.addAll(Arrays.asList(wallHitbox.wallTriangles));
            level.events.sendWallTrianglesAdded(wallHitbox.wallTriangles);
        }

        float lastTime = Time.getCurrentTime();
        while (shouldRun) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // if (Time.getCurrentTime() - lastTime >= level.secondsPerGameTick) {
                // lastTime = Time.getCurrentTime();

                //wall.setPosition(wall.getPosition().x, wall.getPosition().y + 0.05f, wall.getPosition().z);
                //level.events.sendPhysicalObjectMoved(wall.id, wall.getPosition().x, wall.getPosition().y,
                //        wall.getPosition().z);

                // level.update();
            //}
        }
    }

}
