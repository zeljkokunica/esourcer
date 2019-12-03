package org.esourcer.orders.product;

import org.esourcer.orders.common.EntityNotFoundException;
import org.esourcer.orders.product.model.ProductId;

public class ProductNotFoundException extends EntityNotFoundException {

    public ProductNotFoundException(final ProductId productId) {
        super("Product " + productId + " was not found");
    }
}
