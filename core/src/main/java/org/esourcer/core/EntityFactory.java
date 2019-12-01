package org.esourcer.core;

import lombok.Getter;
import org.esourcer.core.entity.EntityManager;
import org.esourcer.core.entity.EntityMangerOptions;
import org.esourcer.core.entity.EntityTransactionManager;
import org.esourcer.core.entity.EntityTransactionManager.VoidEntityTransactionManager;
import org.esourcer.core.entity.PersistentEntity;
import org.esourcer.core.entity.ReplyType;
import org.esourcer.core.events.EventStore;
import org.esourcer.core.events.InMemoryEventStore;
import org.esourcer.core.snapshot.InMemorySnapshotStore;
import org.esourcer.core.snapshot.SnapshotStore;

import java.util.Optional;

public class EntityFactory<Command extends ReplyType, Event, Entity, EntityId> {

    private final PersistentEntity<Command, Event, Entity, EntityId> entity;
    private SnapshotStore<Entity, EntityId> snapshotStore;
    private EventStore<Event, EntityId> eventStore;
    private EntityMangerOptions entityMangerOptions;
    private EntityTransactionManager entityTransactionManager;

    public EntityFactory(final PersistentEntity<Command, Event, Entity, EntityId> entity) {
        this.entity = entity;
    }

    public EntityFactory<Command, Event, Entity, EntityId> withSnapshotStore(
            final SnapshotStore<Entity, EntityId> snapshotStore) {
        this.snapshotStore = snapshotStore;
        return this;
    }

    public EntityFactory<Command, Event, Entity, EntityId> withEventStore(
            final EventStore<Event, EntityId> eventStore) {
        this.eventStore = eventStore;
        return this;
    }

    public EntityFactory<Command, Event, Entity, EntityId> withEntityTransactionManager(
            final EntityTransactionManager entityTransactionManager) {
        this.entityTransactionManager = entityTransactionManager;
        return this;
    }

    public EntityFactory<Command, Event, Entity, EntityId> withEntityMangerOptions(
            final EntityMangerOptions entityMangerOptions) {
        this.entityMangerOptions = entityMangerOptions;
        return this;
    }

    public EntityManager<Command, Event, Entity, EntityId> forEntity(final EntityId entityId) {
        return new EntityManager<>(
                entityId,
                Optional.ofNullable(entityMangerOptions)
                        .orElse(EntityMangerOptions.builder().snapshotBatch(1L).build()),
                Optional.ofNullable(snapshotStore).orElse(new InMemorySnapshotStore<>(entity.name())),
                Optional.ofNullable(eventStore).orElse(new InMemoryEventStore<>(entity.name())),
                Optional.ofNullable(entityTransactionManager).orElse(new VoidEntityTransactionManager()),
                entity::buildBehaviour);
    }


}
