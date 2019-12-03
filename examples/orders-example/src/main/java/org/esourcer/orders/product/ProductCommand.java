package org.esourcer.orders.product;

import lombok.NonNull;
import lombok.Value;
import org.esourcer.core.entity.ReplyType;
import org.esourcer.orders.common.Audit;
import org.esourcer.orders.product.model.Product;
import org.esourcer.orders.common.Money;
import org.esourcer.orders.product.model.ProductId;

import java.util.Optional;

public interface ProductCommand<Result> extends ReplyType<Result> {

    @Value
    class GetProduct implements ProductCommand<Product> {
        @NonNull
        Optional<Long> version;
    }

    @Value
    class CreateProduct implements ProductCommand<ProductId> {

        @NonNull
        String name;

        String description;

        @NonNull
        Money price;

        @NonNull
        Audit audit;
    }

    @Value
    class UpdateProduct implements ProductCommand<Product> {


        @NonNull
        String name;

        String description;

        @NonNull
        Money price;

        @NonNull
        Audit audit;
    }
}
