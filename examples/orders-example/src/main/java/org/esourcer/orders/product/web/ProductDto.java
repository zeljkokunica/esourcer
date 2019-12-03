package org.esourcer.orders.product.web;

import lombok.Value;
import org.esourcer.orders.product.ProductEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Value
public final class ProductDto {

    String id;
    String name;
    String description;
    BigDecimal price;
    String currency;
    Long version;
    Instant createdAt;
    List<ProductDetailsDto> versions;
}
