package org.esourcer.orders.product;

import lombok.RequiredArgsConstructor;
import org.esourcer.core.EntityFactory;
import org.esourcer.orders.common.Audit;
import org.esourcer.orders.common.Money;
import org.esourcer.orders.product.ProductCommand.CreateProduct;
import org.esourcer.orders.product.ProductCommand.GetProduct;
import org.esourcer.orders.product.ProductCommand.UpdateProduct;
import org.esourcer.orders.product.model.Product;
import org.esourcer.orders.product.model.ProductId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final EntityFactory<ProductCommand, ProductEvent, Product, ProductId> productEntityFactory;

    @Transactional
    public Product createProduct(final Audit audit, final String name, final String description, final Money price) {
        final ProductId productId = ProductId.nextId();
        final var productEntity = productEntityFactory.forEntity(productId);
        productEntity.executeCommand(new CreateProduct(name, description, price, audit));
        return productEntity.executeCommand(new GetProduct(Optional.empty())).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public Product getProduct(final Audit audit, final ProductId productId) {
        return productEntityFactory.forEntity(productId)
                .executeCommand(new GetProduct(Optional.empty())).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public Product getActiveProductByOriginalId(final Audit audit, final ProductId originalId) {
        return null;
    }

    @Transactional
    public Product updateProduct(final Audit audit, final ProductId productId,
            final String name, final String description, final Money price) {
        return productEntityFactory.forEntity(productId)
                .executeCommand(new UpdateProduct(name, description, price, audit)).orElseThrow();
    }

    @Transactional
    public List<Product> findAllProducts() {
        return null;
    }

}
