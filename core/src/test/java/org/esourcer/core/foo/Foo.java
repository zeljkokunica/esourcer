package org.esourcer.core.foo;

import lombok.Value;

import java.time.Instant;

@Value
public final class Foo {

    final String id;
    final String name;
    final Instant createdAt;
    final Instant updatedAt;

    public Foo update(final String name, final Instant updatedAt) {
        return new Foo(id, name, createdAt, updatedAt);
    }
}
