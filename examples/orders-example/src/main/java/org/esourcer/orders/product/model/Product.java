package org.esourcer.orders.product.model;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;
import org.esourcer.orders.common.Audit;
import org.esourcer.orders.common.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Value
public final class Product {

    @NonNull
    ProductId id;

    @NonNull
    String name;

    @NonNull
    String description;

    @NonNull
    Money price;

    @NonNull
    Audit created;

    @Wither
    @NonNull
    Boolean active;

    @NonNull
    Long version;

    List<Product> versions;

    public static Product create(final Audit audit, final ProductId productId, final String name, final String description, final Money price) {
        if (!price.greaterThanZero()) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        return new Product(
                productId,
                name,
                description,
                price,
                audit,
                true,
                0L,
                new ArrayList<>()
        );
    }

    public Product update(final Audit audit, final String name, final String description, final Money price) {
        final ArrayList<Product> updated = new ArrayList(versions);
        updated.add(this);
        return new Product(
                id,
                name,
                description,
                price,
                audit,
                true,
                version + 1,
                updated

        );
    }

    public Product deactivate() {
        return withActive(false);
    }

    public Product forVersion(final Optional<Long> version) {
        if (version.isEmpty()) {
            return this;
        }
        return versions.stream()
                .filter(product -> product.getVersion().equals(version.get()))
                .findAny()
                .orElse(null);
    }
}
