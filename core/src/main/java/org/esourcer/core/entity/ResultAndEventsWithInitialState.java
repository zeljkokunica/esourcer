package org.esourcer.core.entity;

import lombok.Value;

import java.util.Optional;

@Value
public final class ResultAndEventsWithInitialState<Result, Event> {
    final ResultAndEvents<Result, Event> resultAndEvents;
    final Optional<Long> lastEventId;
}
