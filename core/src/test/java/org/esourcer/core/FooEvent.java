package org.esourcer.core;

import lombok.Value;

import java.time.Instant;

public interface FooEvent {

    @Value
    class FooCreated implements FooEvent {
        final FooDetails details;
    }

    @Value
    class FooUpdated implements FooEvent {
        final FooDetails details;
    }

    @Value
    class FooDetails {
        final String id;
        final String name;
        final Instant createdAt;
        final Instant updatedAt;
    }
}
