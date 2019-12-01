package org.esourcer.core.events;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class InMemoryEventStore<Event, EntityId> implements EventStore<Event, EntityId> {
    private final String entityEventGroup;
    private final ConcurrentHashMap<String, List<Event>> entityEvents = new ConcurrentHashMap<>();

    @Override
    public Stream<Event> readEventsFrom(final EntityId entityId, final Long fromIndex) {
        final List<Event> events = getEvents(entityId);
        return events.stream().skip(fromIndex);
    }

    @Override
    public Long writeEvents(final EntityId entityId, final List<Event> events) {
        final String entityTopic = entityEventGroup + "#" + entityId;
        final List<Event> existing = getEvents(entityId);
        final List<Event> updatedList = new ArrayList<>(existing);
        updatedList.addAll(events);
        entityEvents.put(entityTopic, updatedList);
        return Long.valueOf(updatedList.size());
    }

    private List<Event> getEvents(final EntityId entityId) {
        final String entityTopic = entityEventGroup + "#" + entityId;
        return entityEvents.containsKey(entityTopic) ? entityEvents.get(entityTopic) : Collections.emptyList();
    }
}
