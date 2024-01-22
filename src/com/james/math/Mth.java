package com.james.math;

import com.james.input.WindowResizeInput;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Mth {

    private static final float FOV = 70;
    private static final float FAR_PLANE = 1000;
    private static final float NEAR_PLANE = 0.1f;

    private static final Random r = new Random();

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

    public static Vector3f multiply(Vector3f vector, float a) {
        return new Vector3f(vector.x * a, vector.y * a, vector.z * a);
    }

    public static Vector2f multiply(Vector2f vector, float a) {
        return new Vector2f(vector.x * a, vector.y * a);
    }

    private static float pythagoreanTheorem(float a, float b) {
        return (float) Math.sqrt(a * a + b * b);
    }

    // TODO: 2023-06-17 Is the abs really necessary?? This is pythagorean theorem we're doing, which should be
    //  positive regardless.
    public static float distance(Vector3f posA, Vector3f posB) {
        float xAndZDistance = Math.abs(pythagoreanTheorem(posA.x - posB.x, posA.z - posB.z));
        float finalDistance = Math.abs(pythagoreanTheorem(xAndZDistance, posA.y - posB.y));
        return finalDistance;
    }

    public static float squaredDistance(Vector3f posA, Vector3f posB) {
        float xAndZDistance = pythagoreanTheorem(posA.x - posB.x, posA.z - posB.z);
        float finalDistance = xAndZDistance * xAndZDistance + (posA.y - posB.y) * (posA.y - posB.y);
        return finalDistance;
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

    public static Vector3f add(Vector3f... vectors) {
        float finalX = 0;
        float finalY = 0;
        float finalZ = 0;

        for (Vector3f vector : vectors) {
            finalX += vector.x;
            finalY += vector.y;
            finalZ += vector.z;
        }

        return new Vector3f(finalX, finalY, finalZ);
    }

    public static Vector2f multiplyVectors(Vector2f a, Vector2f b) {
        return new Vector2f(a.x * b.x, a.y * b.y);
    }

    public static int asScreenCoordForPosition(float normalizedCoord, int screenDimension) {
        return (int) ((normalizedCoord + 1) / 2 * screenDimension);
    }

    public static int asScreenCoordForSize(float normalizedSize, int screenDimension) {
        return (int) (normalizedSize / 2 * screenDimension);
    }

    public static float linearlyInterpolate(float a, float b, float progress) {
        float difference = b - a;
        float amountFromA = difference * progress;
        return a + amountFromA;
    }

    public static boolean pointInTriangle(Vector2f a, Vector2f b, Vector2f c, Vector2f p) {
        float w1Numerator = a.x * (c.y - a.y) + (p.y - a.y) * (c.x - a.x) - p.x * (c.y - a.y);
        float w1Denominator = (b.y - a.y) * (c.x - a.x) - (b.x - a.x) * (c.y - a.y);
        float w1 = w1Numerator / w1Denominator;

        float w2Numerator = p.y - a.y - w1 * (b.y - a.y);
        float w2Denominator = c.y - a.y;
        float w2 = w2Numerator / w2Denominator;

        return w1 >= 0 && w2 >= 0 && w1+w2 <= 1;
    }

    // Note: this method is implemented from Fauerby's report:
    // http://www.peroxide.dk/papers/collision/collision.pdf
    public static boolean pointInTriangle(Vector3f pa, Vector3f pb, Vector3f pc, Vector3f point) {
        Vector3f e10 = Vector3f.sub(pb, pa, null);
        Vector3f e20 = Vector3f.sub(pc, pa, null);

        float a = Vector3f.dot(e10, e10);
        float b = Vector3f.dot(e10, e20);
        float c = Vector3f.dot(e20, e20);
        float ac_bb = a*c - b*b;
        Vector3f vp = new Vector3f(point.x - pa.x, point.y - pa.y, point.z - pa.z);

        float d = Vector3f.dot(vp, e10);
        float e = Vector3f.dot(vp, e20);
        float x = d*c - e*b;
        float y = e*a - d*b;
        float z = x + y - ac_bb;

        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("z = " + z);

        System.out.println("Float.floatToIntBits(x) = " + Float.floatToIntBits(x));
        System.out.println("Float.floatToIntBits(y) = " + Float.floatToIntBits(y));
        System.out.println("Float.floatToIntBits(z) = " + Float.floatToIntBits(z));

        System.out.println("(( Float.floatToIntBits(z) & (~( Float.floatToIntBits(x) | Float.floatToIntBits(y) )) ) & 0x80000000)  = " + ((Float.floatToIntBits(z) & (~(Float.floatToIntBits(x) | Float.floatToIntBits(y)))) & 0x80000000));
        System.out.println("(( (int)z & (~( (int)x | (int)y )) ) & 0x80000000) = " + (((int) z & (~((int) x | (int) y))) & 0x80000000));

        return (( Float.floatToIntBits(z) & (~( Float.floatToIntBits(x) | Float.floatToIntBits(y) )) ) & 0x80000000) != 0;
//        return (( (int)z & (~( (int)x | (int)y )) ) & 0x80000000) != 0;
    }

    // Note: this method is implemented from Fauerby's report:
    // http://www.peroxide.dk/papers/collision/collision.pdf
    public Float getLowestRootUnderThreshold(float a, float b, float c, float maxR) {
        float determinant = b * b - 4 * a * c;
        if (determinant < 0) return null;

        float sqrtOfDeterminant = (float) Math.sqrt(determinant);
        float r1 = (-b - sqrtOfDeterminant) / (2 * a);
        float r2 = (-b + sqrtOfDeterminant) / (2 * a);

        if (r1 > r2) {
            float temp = r2;
            r2 = r1;
            r1 = temp;
        }

        if (r1 > 0 && r1 < maxR) {
            return r1;
        }

        if (r2 > 0 && r2 < maxR) {
            return r2;
        }

        return null;
    }

}
