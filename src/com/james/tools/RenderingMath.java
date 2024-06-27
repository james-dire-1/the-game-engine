package com.james.tools;

import com.james.input.WindowResizeInput;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Utility class that contains math methods that are important for rendering (i.e. they are client-side).
 */
public class RenderingMath {

    private static final float FOV = 70;
    private static final float FAR_PLANE = 1000;
    private static final float NEAR_PLANE = 0.1f;

    public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(position, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f position, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(position, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 0), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createProjectionMatrix(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f matrix = new Matrix4f();
        matrix.m00 = x_scale;
        matrix.m11 = y_scale;
        matrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        matrix.m23 = -1;
        matrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        matrix.m33 = 0;

        return matrix;
    }

    public static Matrix4f createViewMatrix(Vector3f position, float pitch, float yaw, float roll) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1), matrix, matrix);
        Vector3f negativeCameraPosition = new Vector3f(-position.x, -position.y, -position.z);
        Matrix4f.translate(negativeCameraPosition, matrix, matrix);

        return matrix;
    }

    public static Vector2f toNormalizedPosition(int screenX, int screenY) {
        float x = (float) screenX / WindowResizeInput.width * 2 - 1;
        float y = (float) screenY / WindowResizeInput.height * 2 - 1;

        return new Vector2f(x, y);
    }

    public static Vector2f toNormalizedSize(int screenX, int screenY) {
        float x = (float) screenX / WindowResizeInput.width * 2;
        float y = (float) screenY / WindowResizeInput.height * 2;

        return new Vector2f(x, y);
    }

    public static int asScreenCoordForPosition(float normalizedCoord, int screenDimension) {
        return (int) ((normalizedCoord + 1) / 2 * screenDimension);
    }

    public static int asScreenCoordForSize(float normalizedSize, int screenDimension) {
        return (int) (normalizedSize / 2 * screenDimension);
    }


    public static Vector2f add(Vector2f a, Vector2f b) {
        return Vector2f.add(a, b, null);
    }

    public static Vector2f add(Vector2f... vectors) {
        float finalX = 0;
        float finalY = 0;

        for (Vector2f vector : vectors) {
            finalX += vector.x;
            finalY += vector.y;
        }

        return new Vector2f(finalX, finalY);
    }

    public static Vector2f multiplyVectors(Vector2f a, Vector2f b) {
        return new Vector2f(a.x * b.x, a.y * b.y);
    }

    public static float linearlyInterpolate(float a, float b, float progress) {
        float difference = b - a;
        float amountFromA = difference * progress;
        return a + amountFromA;
    }

}
