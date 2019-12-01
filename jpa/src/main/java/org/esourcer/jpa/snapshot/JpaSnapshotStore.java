package org.esourcer.jpa.snapshot;

import lombok.SneakyThrows;
import org.esourcer.core.serialization.Serializer;
import org.esourcer.core.snapshot.SnapshotStore;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Optional;
import javax.persistence.EntityManager;

public class JpaSnapshotStore<Entity, EntityId> implements SnapshotStore<Entity, EntityId> {

    private final String eventGroup;
    private final EntityManager entityManager;
    private final Serializer serializer;

    public JpaSnapshotStore(final String eventGroup, final EntityManager entityManager,
            final Serializer serializer) {
        this.eventGroup = eventGroup;
        this.entityManager = entityManager;
        this.serializer = serializer;
    }

    @Override
    public Optional<Long> snapshotVersion(final EntityId entityId) {
        return entityManager.createNamedQuery("JpaSnapshot.findByEventGroupAndEntityId", JpaSnapshot.class)
                .setParameter("eventGroup", eventGroup)
                .setParameter("entityId", entityId.toString())
                .getResultStream()
                .map(JpaSnapshot::getLastAppliedEvent)
                .findAny();
    }

    @Override
    public Optional<Entity> recover(final EntityId entityId) {
        return findSnapshot(entityId)
                .map(this::toEntity);
    }

    @Override
    public void createSnapshot(final Long version, final EntityId entityId, final Entity entity) {
        final JpaSnapshot snapshot = findSnapshot(entityId)
                .orElse(new JpaSnapshot(
                        null,
                        eventGroup,
                        entityId.toString(),
                        null,
                        null,
                        null,
                        null
                ));
        snapshot.setLastAppliedEvent(version);
        snapshot.setSnapshot(new String(serializer.serialize(entity), Charset.forName("UTF-8")));
        snapshot.setSnapshotClassName(entity.getClass().getName());
        snapshot.setCreatedAt(Instant.now());
        entityManager.merge(snapshot);
    }

    @SneakyThrows
    private Entity toEntity(final JpaSnapshot jpaSnapshot) {
        return serializer.deserialize(
                jpaSnapshot.getSnapshot().getBytes(Charset.forName("UTF-8")),
                jpaSnapshot.getSnapshotClassName());
    }

    private Optional<JpaSnapshot> findSnapshot(final EntityId entityId) {
        return entityManager.createNamedQuery("JpaSnapshot.findByEventGroupAndEntityId", JpaSnapshot.class)
                .setParameter("eventGroup", eventGroup)
                .setParameter("entityId", entityId.toString())
                .getResultStream()
                .findAny();
    }
}
