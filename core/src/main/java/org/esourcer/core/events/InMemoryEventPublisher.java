package org.esourcer.core.events;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class InMemoryEventPublisher<Event> implements EventPublisher<Event> {

    private final List<Event> publishedEvents = new ArrayList<>();

    @Override
    public void publish(final List<Event> events) {
        publishedEvents.addAll(events);
    }

    public List<Event> getPublishedEvents() {
        return publishedEvents;
    }
}
