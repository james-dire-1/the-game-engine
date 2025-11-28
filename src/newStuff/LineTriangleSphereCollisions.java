package newStuff;

import com.james.common.simulation.collisionEngine.math.CollisionMath;
import com.james.common.simulation.collisionEngine.math.Plane;
import com.james.common.tools.Mth;
import org.lwjgl.util.vector.Vector3f;

public class LineTriangleSphereCollisions {

    public static void something() {

    }

    // The algorithm was obtained from this video by Jorge Rodriguez:
    // https://www.youtube.com/watch?v=fIu_8b2n8ZM
    private static Vector3f rayAndPlaneIntersection(Ray ray, Plane plane) {
        Vector3f x0 = ray.origin;
        Vector3f v = ray.direction;
        Vector3f n = plane.normal;
        Vector3f w = Vector3f.sub(plane.origin, ray.origin, null);

        float numerator = Vector3f.dot(w, n);
        float denominator = Vector3f.dot(v, n);
        Vector3f term = Mth.multiply(v, numerator / denominator);

        return Vector3f.add(x0, term, null);
    }

}
