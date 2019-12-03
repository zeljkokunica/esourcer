package org.esourcer.orders.common;

import lombok.Value;

import java.util.UUID;

@Value
public final class CustomerId {

    String id;

    public static CustomerId nextId() {
        return new CustomerId(UUID.randomUUID().toString());
    }
}
