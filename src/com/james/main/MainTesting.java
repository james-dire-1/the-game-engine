package com.james.main;

import com.james.math.Mth;
import org.lwjgl.util.vector.Vector3f;

public class MainTesting {

//    public static void main(String[] args) {
//        Vector3f pa = new Vector3f(0, 0, 0);
//        Vector3f pb = new Vector3f(10, 0, 0);
//        Vector3f pc = new Vector3f(5, 10, 0);
//        Vector3f point = new Vector3f(5, 5, 1);
//        boolean isInTriangle = Mth.pointInTriangle(pa, pb, pc, point);

//        System.out.println(isInTriangle);
//    }

//    public static boolean pointInTriangle(Vector3f a, Vector3f b, Vector3f c, Vector3f p) {
//        float denominator = Mth.add(
//                Vector3f.cross(a, b, null),
//                Vector3f.cross(b, c, null),
//                Vector3f.cross(c, a, null)
//        );
//    }

    public static void main(String[] args) {
        Vector3f a = new Vector3f(0, 0, 0);
        Vector3f b = new Vector3f(10, 0, 0);
        Vector3f c = new Vector3f(5, 10, 0);
        Vector3f point = new Vector3f(5, 9.9999f, 0);

        boolean isInTriangle = Mth.pointInTriangle(a, b, c, point);

        System.out.println(isInTriangle);
    }

}
