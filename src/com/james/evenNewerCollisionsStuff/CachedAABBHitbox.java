package com.james.evenNewerCollisionsStuff;

import com.james.main.clientSide.CachedPhysicalObject;
import com.james.main.clientSide.ClientPacketReceiveActions;
import org.lwjgl.util.vector.Vector3f;

// TODO: 2024-06-16 This class is unacceptably identical to AABBHitbox. Merge them somehow!
public class CachedAABBHitbox {

    public final CachedPhysicalObject object;
    public final String meshPath;
    private final ModelMesh mesh;

    public float lowerX, upperX, lowerY, upperY, lowerZ, upperZ;

    public CachedAABBHitbox(int idOfCorrespondingObject, String meshPath) {
        this.object = ClientPacketReceiveActions.cachedLocalPhysicalObjects.get(idOfCorrespondingObject);
        this.meshPath = meshPath;
        this.mesh = ModelMeshBankInR3.getModelMesh(meshPath);

        updatePosition();
    }

    public void updatePosition() {
        Vector3f objectPosition = object.getPosition();

        lowerX = objectPosition.x + mesh.minX;
        upperX = objectPosition.x + mesh.maxX;
        lowerY = objectPosition.y + mesh.minY;
        upperY = objectPosition.y + mesh.maxY;
        lowerZ = objectPosition.z + mesh.minZ;
        upperZ = objectPosition.z + mesh.maxZ;
    }

}
