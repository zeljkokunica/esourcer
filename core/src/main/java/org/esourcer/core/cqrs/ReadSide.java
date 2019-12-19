package org.esourcer.core.cqrs;

public interface ReadSide<Event> {

    void processEvent(final Long eventIndex, final Event event);

}
