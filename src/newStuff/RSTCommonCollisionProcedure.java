package newStuff;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractAABBHitbox;
import com.james.common.simulation.collisionEngine.math.Plane;
import com.james.common.tools.Mth;
import org.lwjgl.util.vector.Vector3f;

public class RSTCommonCollisionProcedure {

    // The algorithm and code were obtained from these videos by Jorge Rodriguez:
    // https://www.youtube.com/watch?v=USjbg5QXk3g
    // https://www.youtube.com/watch?v=3vONlLYtHUE
    public static Vector3f rayAndAABBIntersection(Ray ray, float rayLength, AbstractAABBHitbox aabb) {
        Vector3f rayStart = ray.origin;
        Vector3f rayEnd = Vector3f.add(ray.origin, Mth.multiply(ray.direction, rayLength), null);
        ClipLineInfo clipLineInfo = new ClipLineInfo();

        boolean possiblyInAABB = clipLine(rayStart.x, rayEnd.x, aabb.lowerX, aabb.upperX, clipLineInfo);
        if (!possiblyInAABB) return null;

        boolean stillPossiblyInAABB = clipLine(rayStart.y, rayEnd.y, aabb.lowerY, aabb.upperY, clipLineInfo);
        if (!stillPossiblyInAABB) return null;

        boolean definitelyInAABB = clipLine(rayStart.z, rayEnd.z, aabb.lowerZ, aabb.upperZ, clipLineInfo);
        if (!definitelyInAABB) return null;

        Vector3f b = Vector3f.sub(rayEnd, rayStart, null);
        return Vector3f.add(rayStart, Mth.multiply(b, clipLineInfo.fLower), null);
    }

    // The algorithm and code were obtained from these videos by Jorge Rodriguez:
    // https://www.youtube.com/watch?v=USjbg5QXk3g
    // https://www.youtube.com/watch?v=3vONlLYtHUE
    private static boolean clipLine(float rayStartDim, float rayEndDim, float aabbLowerDim, float aabbUpperDim, ClipLineInfo clipLineInfo) {
        float fLowerDim = (aabbLowerDim - rayStartDim) / (rayEndDim - rayStartDim);
        float fUpperDim = (aabbUpperDim - rayStartDim) / (rayEndDim - rayStartDim);

        if (fLowerDim > fUpperDim) {
            float temporary = fLowerDim;
            fLowerDim = fUpperDim;
            fUpperDim = temporary;
        }

        if (fUpperDim < clipLineInfo.fLower || fLowerDim > clipLineInfo.fUpper) {
            return false;
        }

        clipLineInfo.fLower = Math.max(clipLineInfo.fLower, fLowerDim);
        clipLineInfo.fUpper = Math.min(clipLineInfo.fUpper, fUpperDim);

        return clipLineInfo.fLower <= clipLineInfo.fUpper;
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

    private static class ClipLineInfo {
        private float fLower;
        private float fUpper;
    }

}
