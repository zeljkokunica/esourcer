package org.esourcer.core.entity;

import lombok.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
public final class ResultAndEvents<Result, Event> {
    final Optional<Result> result;
    final List<Event> events;

    public static <Result, Event> ResultAndEvents<Result, Event> of(final Result result, final Event... event) {
        return new ResultAndEvents<>(Optional.ofNullable(result), Arrays.asList(event));
    }

    public static <Result, Event> ResultAndEvents<Result, Event> ofResult(final Result entity) {
        return new ResultAndEvents<>(Optional.ofNullable(entity), Collections.emptyList());
    }

    public static <Result, Event> ResultAndEvents<Result, Event> ofEvent(final Event event) {
        return new ResultAndEvents<>(Optional.empty(), Collections.singletonList(event));
    }

    public static <Result, Event> ResultAndEvents<Result, Event> ofEvents(final List<Event> events) {
        return new ResultAndEvents<>(Optional.empty(), events);
    }
}
