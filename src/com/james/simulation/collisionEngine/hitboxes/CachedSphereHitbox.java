package com.james.simulation.collisionEngine.hitboxes;

import com.james.common.simulation.collisionEngine.hitboxes.AbstractSphereHitbox;
import com.james.simulation.ClientLevel;

import java.util.Objects;

public class CachedSphereHitbox extends AbstractSphereHitbox {

    public CachedSphereHitbox(int idOfCorrespondingObject, float radius) {
        super(ClientLevel.get().getCachedPhysicalObject(idOfCorrespondingObject), radius);
    }

    public static class Identifier {
        public final int cachedPhysicalObjectId;
        public final float radius;

        public Identifier(int cachedPhysicalObjectId, float radius) {
            this.cachedPhysicalObjectId = cachedPhysicalObjectId;
            this.radius = radius;
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
                    (this.radius == other.radius);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cachedPhysicalObjectId, radius);
        }
    }

}
