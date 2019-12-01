package org.esourcer.core.entity;

import org.esourcer.core.events.EventStore;
import org.esourcer.core.snapshot.SnapshotStore;

import java.util.Optional;
import java.util.function.BiFunction;

public class EntityManager<Command extends ReplyType, Event, Entity, EntityId> {
    private final EntityId entityId;
    private final EntityMangerOptions options;
    private final SnapshotStore<Entity, EntityId> snapshotManager;
    private final EventStore<Event, EntityId> eventStore;
    private final Behaviour behaviour;
    private final EntityTransactionManager entityTransactionManager;

    public EntityManager(
            final EntityId entityId,
            final EntityMangerOptions options,
            final SnapshotStore<Entity, EntityId> snapshotManager,
            final EventStore<Event, EntityId> eventStore,
            final EntityTransactionManager entityTransactionManager,
            final BiFunction<EntityManager, Behaviour, Behaviour> behaviourBuilder) {
        this.entityId = entityId;
        this.options = options;
        this.snapshotManager = Optional.ofNullable(snapshotManager).orElse(new SnapshotStore.VoidSnapshotManager());
        this.eventStore = eventStore;
        behaviour = behaviourBuilder.apply(this, new Behaviour<>());
        this.entityTransactionManager = entityTransactionManager;
    }

    public <Reply> Optional<Reply> executeCommand(final ReplyType<Reply> command) {
        return entityTransactionManager.executeInTransaction(() -> {
            final ResultAndEvents<Reply, Event> result = executeCommandInternal(command);
            if (!result.getEvents().isEmpty()) {
                final Optional<Long> lastSnapshotVersion = snapshotManager.snapshotVersion(entityId);
                final Long lastEventVersion = eventStore.writeEvents(entityId, result.getEvents());
                final Optional<Entity> resultingEntity = recoverEntity();
                final Long pendingBatchSize = lastEventVersion - lastSnapshotVersion.orElse(0L);
                if (pendingBatchSize >= options.getSnapshotBatch()) {
                    snapshotManager.createSnapshot(lastEventVersion, entityId, resultingEntity.get());
                }
            }
            return result.getResult();
        });
    }

    public Optional<Entity> recoverEntity() {
        final Optional<Long> snapshotVersion = snapshotManager.snapshotVersion(entityId);
        final Optional<Entity> snapshot = snapshotManager.recover(entityId);
        return eventStore.readEventsFrom(entityId, snapshotVersion.orElse(0L))
                .reduce(
                        snapshot,
                        (previous, event) -> applyEvent(previous, event),
                        (old, updated) -> updated);
    }

    public EntityId getEntityId() {
        return entityId;
    }

    private <Reply> ResultAndEvents<Reply, Event> executeCommandInternal(final ReplyType<Reply> command) {
        final Optional<Entity> entity = recoverEntity();
        final ResultAndEvents<Reply, Event> resultAndEvents = onCommand(entityId, entity, command);
        return new ResultAndEvents(resultAndEvents.getResult(), resultAndEvents.getEvents());
    }

    private <Reply> ResultAndEvents<Reply, Event> onCommand(final EntityId entityId, final Optional<Entity> entity,
            final ReplyType<Reply> command) {
        return behaviour.handlesCommand(command.getClass())
                ? (ResultAndEvents<Reply, Event>) behaviour.commandHandler(command.getClass()).apply(command)
                : null;
    }

    private Optional<Entity> applyEvent(final Optional<Entity> entity, final Event event) {
        return behaviour.handlesEvent(event.getClass())
                ? (Optional<Entity>) behaviour.eventHandler(event.getClass()).apply(entity, event)
                : null;
    }

}
