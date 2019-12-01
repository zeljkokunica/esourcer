package org.esourcer.core;

import lombok.Value;
import org.esourcer.core.entity.ReplyType;

import java.time.Instant;

public interface FooCommand<Result> extends ReplyType<Result> {

    @Value
    class GetFoo implements FooCommand<Foo> {
    }

    @Value
    class CreateFoo implements FooCommand<String> {
        final String id;
        final String name;
        final Instant createdAt;
    }

    @Value
    class UpdateFoo implements FooCommand<Foo> {
        final String name;
        final Instant updatedAt;
    }
}
