package org.esourcer.core.snapshot;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InMemorySnapshotStore<Entity, EntityId> implements SnapshotStore<Entity, EntityId> {
    private final String entityEventGroup;
    private final ConcurrentHashMap<String, Snapshot> snapshots = new ConcurrentHashMap<>();

    class Snapshot {
        private Long version;
        private Entity entity;

        public Snapshot(Long version, Entity entity) {
            this.version = version;
            this.entity = entity;
        }

        public Long getVersion() {
            return version;
        }

        public Entity getEntity() {
            return entity;
        }
    }

    @Override
    public Optional<Long> snapshotVersion(final EntityId entityId) {
        final String entityTopic = entityEventGroup + "#" + entityId;
        return Optional.ofNullable(snapshots.containsKey(entityTopic)
                ? Optional.ofNullable(snapshots.get(entityTopic)).map(Snapshot::getVersion).orElse(null)
                : null);
    }

    @Override
    public Optional<Entity> recover(final EntityId entityId) {
        final String entityTopic = entityEventGroup + "#" + entityId;
        return Optional.ofNullable(snapshots.containsKey(entityTopic)
                ? Optional.ofNullable(snapshots.get(entityTopic)).map(Snapshot::getEntity).orElse(null)
                : null);
    }

    @Override
    public void createSnapshot(final Long version, final EntityId entityId, final Entity entity) {
        final String entityTopic = entityEventGroup + "#" + entityId;
        snapshots.put(entityTopic, new Snapshot(version, entity));
    }
}
