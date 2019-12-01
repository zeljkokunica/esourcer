package org.esourcer.core.snapshot;

import java.util.Optional;

public interface SnapshotStore<Entity, EntityId> {

    Optional<Long> snapshotVersion(final EntityId entityId);

    Optional<Entity> recover(final EntityId entityId);

    void createSnapshot(final Long version, final EntityId entityId, final Entity entity);

    class VoidSnapshotManager<Entity, EntityId> implements SnapshotStore<Entity, EntityId> {

        @Override
        public Optional<Long> snapshotVersion(EntityId entityId) {
            return Optional.empty();
        }

        @Override
        public Optional<Entity> recover(EntityId entityId) {
            return Optional.empty();
        }

        @Override
        public void createSnapshot(Long version, final EntityId entityId, Entity entity) {
        }
    }
}
