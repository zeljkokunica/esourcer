package org.esourcer.orders.product.web;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public final class CreateProductRequest {

    @NonNull
    String name;
    String description;
    @NonNull
    BigDecimal price;
    @NonNull
    String currency;
}
