package org.esourcer.core.events;

import java.util.List;

public interface EventPublisher<Event> {

    void publish(final List<Event> events);

}
