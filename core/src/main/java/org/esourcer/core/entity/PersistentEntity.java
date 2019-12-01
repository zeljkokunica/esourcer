package org.esourcer.core.entity;

public interface PersistentEntity<Command extends ReplyType, Event, Entity, EntityId> {

    String name();

    Behaviour<Command, Event, Entity> buildBehaviour(
            final EntityManager<Command, Event, Entity, EntityId> entityManager,
            final Behaviour<Command, Event, Entity> behaviour);
}
