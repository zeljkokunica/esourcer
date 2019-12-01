package org.esourcer.core.entity;

import lombok.Getter;
import org.esourcer.core.events.EventStore;
import org.esourcer.core.snapshot.SnapshotStore;

import java.util.Optional;
import java.util.function.BiFunction;

@Getter
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
            final ResultAndEventsWithInitialState<Reply, Event> result = executeCommandInternal(command);
            if (!result.getResultAndEvents().getEvents().isEmpty()) {
                final Optional<Long> lastSnapshotVersion = snapshotManager.snapshotVersion(entityId);
                final Long lastEventVersion = eventStore.writeEvents(
                        entityId,
                        result.getLastEventId().map(aLong -> aLong + 1).orElse(0L),
                        result.getResultAndEvents().getEvents());
                final Optional<Entity> resultingEntity = getEntity();
                final Long pendingBatchSize = lastEventVersion - lastSnapshotVersion.orElse(0L);
                if (pendingBatchSize >= options.getSnapshotBatch()) {
                    snapshotManager.createSnapshot(lastEventVersion, entityId, resultingEntity.get());
                }
            }
            return result.getResultAndEvents().getResult();
        });
    }

    public Optional<Entity> getEntity() {
        return recoverEntity().getEntity();
    }

    private CurrentEntityState<Entity> recoverEntity() {
        final Optional<Long> snapshotVersion = snapshotManager.snapshotVersion(entityId);
        final Optional<Entity> snapshot = snapshotManager.recover(entityId);
        final CurrentEntityState<Entity> currentEntityState = new CurrentEntityState<>(
                snapshotVersion,
                snapshot
        );
        return eventStore.readEventsFrom(entityId, snapshotVersion.orElse(0L))
                .reduce(
                        currentEntityState,
                        (previous, event) -> applyEvent(previous, event),
                        (old, updated) -> updated);
    }

    public EntityId getEntityId() {
        return entityId;
    }

    private <Reply> ResultAndEventsWithInitialState<Reply, Event> executeCommandInternal(
            final ReplyType<Reply> command) {
        final CurrentEntityState<Entity> currentEntityState = recoverEntity();
        final ResultAndEvents<Reply, Event> resultAndEvents = onCommand(entityId, currentEntityState.getEntity(),
                command);
        return new ResultAndEventsWithInitialState(resultAndEvents, currentEntityState.getLastEventId());
    }

    private <Reply> ResultAndEvents<Reply, Event> onCommand(final EntityId entityId, final Optional<Entity> entity,
            final ReplyType<Reply> command) {
        return behaviour.handlesCommand(command.getClass())
                ? (ResultAndEvents<Reply, Event>) behaviour.commandHandler(command.getClass()).apply(command)
                : null;
    }

    private CurrentEntityState<Entity> applyEvent(final CurrentEntityState<Entity> currentEntityState,
            final Event event) {
        return behaviour.handlesEvent(event.getClass())
                ? currentEntityState.applyEntityUpdate(
                    (Optional<Entity>) behaviour.eventHandler(event.getClass())
                        .apply(currentEntityState.getEntity(), event))
                : null;
    }

}
