package com.james.main;

import com.james.collisions.AbstractPhysicalObject;
import com.james.input.KeyInput;
import com.james.math.Mth;
import com.james.renderEngine.gameObjects.Camera;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends AbstractPhysicalObject {

    private static final float SPEED = 10;

    private final Vector3f rotation = new Vector3f();

    public Vector3f getRotation() { return rotation; }

    private final Camera camera;
    private final Vector2f forwardDirectionVector = new Vector2f();
    private final Vector2f rightDirectionVector = new Vector2f();

    public Player(Vector3f position) {
        super(position);

        Model model = ModelBank.getAbstractArt();
        GameObject gameObject = new GameObject(model, super.position, this.rotation, 1);
        Main.batchedGameObjectsList.addGameObject(gameObject);

        CameraController camController = new CameraController(Main.focusCamera, gameObject.getPosition(), 20);
        Main.camController = camController;

        this.camera = camController.getCamera();

        calculateDirectionVectors();
    }

    public void update() {
        rotation.y = -camera.getYaw();
        calculateDirectionVectors();

        float forwardSpeed = 0;
        if (KeyInput.isKeyPressed(GLFW_KEY_W)) {
            forwardSpeed += SPEED;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_S)) {
            forwardSpeed -= SPEED;
        }
        float rightSpeed = 0;
        if (KeyInput.isKeyPressed(GLFW_KEY_D)) {
            rightSpeed += SPEED;
        }
        if (KeyInput.isKeyPressed(GLFW_KEY_A)) {
            rightSpeed -= SPEED;
        }

        Vector2f movementPerSecond = Vector2f.add(Mth.multiply(forwardDirectionVector, forwardSpeed), Mth.multiply(rightDirectionVector, rightSpeed), null);

        position.x += movementPerSecond.x * Time.getDeltaTime();
        position.z -= movementPerSecond.y * Time.getDeltaTime();
    }

    private void calculateDirectionVectors() {
        float forwardAngleInUnitCircle = 90 - camera.getYaw();
        forwardDirectionVector.x = (float) Math.cos(Math.toRadians(forwardAngleInUnitCircle));
        forwardDirectionVector.y = (float) Math.sin(Math.toRadians(forwardAngleInUnitCircle));

        float rightAngleInUnitCircle = -camera.getYaw();
        rightDirectionVector.x = (float) Math.cos(Math.toRadians(rightAngleInUnitCircle));
        rightDirectionVector.y = (float) Math.sin(Math.toRadians(rightAngleInUnitCircle));
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

}
