package org.esourcer.orders.product.web;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Value
public final class ProductDetailsDto {

    String id;
    String name;
    String description;
    BigDecimal price;
    String currency;
    Long version;
    Instant createdAt;
}
