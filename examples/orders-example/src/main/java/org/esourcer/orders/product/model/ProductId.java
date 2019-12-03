package org.esourcer.orders.product.model;

import lombok.Value;

import java.util.UUID;

@Value
public final class ProductId {

    String id;

    public static ProductId nextId() {
        return new ProductId(UUID.randomUUID().toString());
    }
}
