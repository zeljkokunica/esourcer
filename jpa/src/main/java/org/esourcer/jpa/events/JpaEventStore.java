package org.esourcer.jpa.events;

import lombok.SneakyThrows;
import org.esourcer.core.events.EventStore;
import org.esourcer.core.serialization.Serializer;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

public class JpaEventStore<Event, EntityId> implements EventStore<Event, EntityId> {

    private final String eventGroup;
    private final EntityManager entityManager;
    private final Serializer serializer;

    public JpaEventStore(
            final EntityManager entityManager,
            final String eventGroup,
            final Serializer serializer) {
        this.eventGroup = eventGroup;
        this.entityManager = entityManager;
        this.serializer = serializer;
    }

    @Override
    public Stream<Event> readEventsFrom(final EntityId entityId, final Long fromIndex) {
        return entityManager.createNamedQuery("JpaEvent.findEventsByGroupAndEntityId", JpaEvent.class)
                .setParameter("eventGroup", eventGroup)
                .setParameter("entityId", entityId.toString())
                .setFirstResult(fromIndex.intValue())
                .getResultStream()
                .map(this::mapFromJpa);
    }

    @Override
    public Long writeEvents(final EntityId entityId, final Long startIndex, final List<Event> events) {
        final AtomicLong atomicLong = new AtomicLong(startIndex);
        events.stream()
                .map(event -> {
                    final Long ordinal = atomicLong.addAndGet(1L);
                    return eventToJpa(eventGroup, entityId, ordinal, event);
                })
                .forEach(jpaEvent -> entityManager.persist(jpaEvent));
        return atomicLong.get();
    }

    private JpaEvent eventToJpa(final String eventGroup, final EntityId entityId, final Long ordinal, final Event event) {
        return new JpaEvent(
                null,
                eventGroup,
                ordinal,
                entityId.toString(),
                event.getClass().getName(),
                new String(serializer.serialize(event), Charset.forName("UTF-8")),
                Instant.now());
    }

    @SneakyThrows
    private Event mapFromJpa(final JpaEvent jpaEvent) {
        return (Event) serializer.deserialize(
                jpaEvent.getEventBody().getBytes(Charset.forName("UTF-8")),
                jpaEvent.getEventClassName());
    }
}
