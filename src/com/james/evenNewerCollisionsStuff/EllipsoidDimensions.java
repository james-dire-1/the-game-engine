package com.james.evenNewerCollisionsStuff;

import com.james.world.Triangle;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class EllipsoidDimensions {

    public final Vector3f radius;

    public final Map<String, ModelMesh> modelMeshMap = new HashMap<>();

    public EllipsoidDimensions(float radiusX, float radiusY, float radiusZ) {
        this.radius = new Vector3f(radiusX, radiusY, radiusZ);

        for (Map.Entry<String, ModelMesh> entry : ModelMeshBankInR3.getModelMeshMap().entrySet()) {
            ModelMesh modelMeshInR3 = entry.getValue();
            ModelMesh modelMeshInEllipsoidSpace = convertFromR3ToEllipsoidSpace(modelMeshInR3);

            modelMeshMap.put(entry.getKey(), modelMeshInEllipsoidSpace);
        }
    }

    private ModelMesh convertFromR3ToEllipsoidSpace(ModelMesh modelMeshInR3) {
        Triangle[] trianglesInR3 = modelMeshInR3.triangles;
        Triangle[] trianglesInEllipsoidSpace = ModelMesh.performOperationOnAllTriangles(trianglesInR3, PointOperations::dividePointByEllipsoidRadius, radius);

        return new ModelMesh(trianglesInEllipsoidSpace);
    }

    public boolean isOfRadius(float radiusX, float radiusY, float radiusZ) {
        return (radiusX == radius.x) && (radiusY == radius.y) && (radiusZ == radius.z);
    }

}
