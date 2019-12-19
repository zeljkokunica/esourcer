package org.esourcer.orders.product;

import org.esourcer.core.entity.Behaviour;
import org.esourcer.core.entity.EntityManager;
import org.esourcer.core.entity.PersistentEntity;
import org.esourcer.orders.common.Audit;
import org.esourcer.orders.product.ProductCommand.CreateProduct;
import org.esourcer.orders.product.ProductCommand.GetProduct;
import org.esourcer.orders.product.ProductCommand.UpdateProduct;
import org.esourcer.orders.product.ProductEvent.ProductCreated;
import org.esourcer.orders.product.ProductEvent.ProductDetails;
import org.esourcer.orders.product.ProductEvent.ProductUpdated;
import org.esourcer.orders.product.model.Product;
import org.esourcer.orders.product.model.ProductId;

import java.util.Optional;

import static org.esourcer.core.entity.ResultAndEvents.of;
import static org.esourcer.core.entity.ResultAndEvents.ofResult;

public class ProductEntity implements PersistentEntity<ProductCommand, ProductEvent, Product, ProductId> {

    @Override
    public String name() {
        return "product";
    }

    @Override
    public Behaviour<ProductCommand, ProductEvent, Product> buildBehaviour(
            final EntityManager<ProductCommand, ProductEvent, Product, ProductId> entityManager,
            final Behaviour<ProductCommand, ProductEvent, Product> behaviour) {
        // get
        behaviour.setCommandHandler(GetProduct.class, cmd -> {
            final Product product = entityManager.getEntity().orElseThrow(() -> new ProductNotFoundException(entityManager.getEntityId()));
            return ofResult(
                    product.forVersion(cmd.getVersion()));
        });

        // create
        behaviour.setCommandHandler(CreateProduct.class, cmd -> of(
                entityManager.getEntityId(),
                new ProductCreated(
                        new ProductDetails(
                                entityManager.getEntityId().getId(),
                                cmd.getName(),
                                cmd.getDescription(),
                                cmd.getPrice()
                        ),
                        cmd.getAudit().getInitiatedAt())));
        behaviour.setEventHandler(ProductCreated.class, (product, evt) -> Optional.of(
                Product.create(
                        new Audit("", evt.getCreatedAt()),
                        entityManager.getEntityId(),
                        evt.getDetails().getName(),
                        evt.getDetails().getDescription(),
                        evt.getDetails().getPrice()))
        );

        // update
        behaviour.setCommandHandler(UpdateProduct.class, cmd -> {
            final Product initial = entityManager.getEntity()
                    .orElseThrow(() -> new ProductNotFoundException(entityManager.getEntityId()));
            final Product updated = initial
                    .update(cmd.getAudit(), cmd.getName(), cmd.getDescription(), cmd.getPrice());
            return of(
                    updated,
                    new ProductUpdated(
                            new ProductDetails(
                                    entityManager.getEntityId().getId(),
                                    updated.getName(),
                                    updated.getDescription(),
                                    updated.getPrice()),
                            updated.getId().getId(),
                            cmd.getAudit().getInitiatedAt()));
        });
        behaviour.setEventHandler(ProductUpdated.class, (product, evt) -> Optional.of(
                product
                        .orElseThrow(() -> new ProductNotFoundException(entityManager.getEntityId()))
                        .update(
                                new Audit("", evt.getUpdatedAt()),
                                evt.getDetails().getName(),
                                evt.getDetails().getDescription(),
                                evt.getDetails().getPrice())
        ));
        return behaviour;
    }
}
