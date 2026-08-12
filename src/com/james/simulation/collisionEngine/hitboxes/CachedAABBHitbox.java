package com.james.simulation.collisionEngine.hitboxes;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractAABBHitbox;
import com.james.simulation.ClientLevel;

import java.util.Objects;

/**
 * AABB hitbox to be used client-side.
 */
public class CachedAABBHitbox extends AbstractAABBHitbox {

    public CachedAABBHitbox(int idOfCorrespondingObject, String meshPath, int subMeshIdentifier) {
        super(ClientLevel.get().getCachedPhysicalObject(idOfCorrespondingObject), meshPath, subMeshIdentifier);
    }

    public static class Identifier {
        public final int cachedPhysicalObjectId;
        public final String meshPath;
        public final int subMeshIdentifier;

        public Identifier(int cachedPhysicalObjectId, String meshPath, int subMeshIdentifier) {
            this.cachedPhysicalObjectId = cachedPhysicalObjectId;
            this.meshPath = meshPath;
            this.subMeshIdentifier = subMeshIdentifier;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }

            Identifier other = (Identifier) object;

            return
                    (this.cachedPhysicalObjectId == other.cachedPhysicalObjectId) &&
                    (this.meshPath.equals(other.meshPath)) &&
                    (this.subMeshIdentifier == other.subMeshIdentifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cachedPhysicalObjectId, meshPath, subMeshIdentifier);
        }
    }

}
