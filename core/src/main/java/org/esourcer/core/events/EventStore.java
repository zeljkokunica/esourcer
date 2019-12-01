package org.esourcer.core.events;

import java.util.List;
import java.util.stream.Stream;

public interface EventStore<Event, EntityId> {

    Stream<Event> readEventsFrom(final EntityId entityId, final Long fromIndex);

    Long writeEvents(final EntityId entityId, final List<Event> events);
}
