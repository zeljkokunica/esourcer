package org.esourcer.orders.product;

import lombok.Value;
import org.esourcer.orders.common.Money;

import java.time.Instant;

public interface ProductEvent {

    @Value
    class ProductCreated implements ProductEvent {
        ProductDetails details;
        Instant createdAt;
    }

    @Value
    class ProductUpdated implements ProductEvent {
        ProductDetails details;
        String newProductId;
        Instant updatedAt;
    }

    @Value
    class ProductDetails {
        String id;
        String name;
        String description;
        Money price;
    }
}
