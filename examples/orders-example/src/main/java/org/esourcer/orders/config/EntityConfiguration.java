package org.esourcer.orders.config;

import lombok.RequiredArgsConstructor;
import org.esourcer.core.EntityFactory;
import org.esourcer.core.entity.EntityMangerOptions;
import org.esourcer.core.entity.PersistentEntity;
import org.esourcer.core.entity.ReplyType;
import org.esourcer.core.serialization.JacksonSerializer;
import org.esourcer.jpa.events.JpaEventStore;
import org.esourcer.jpa.snapshot.JpaSnapshotStore;
import org.esourcer.orders.product.model.Product;
import org.esourcer.orders.product.ProductCommand;
import org.esourcer.orders.product.ProductEntity;
import org.esourcer.orders.product.ProductEvent;
import org.esourcer.orders.product.model.ProductId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Configuration
public class EntityConfiguration {

    private final EntityManager entityManager;
    private final JacksonSerializer jacksonSerializer;

    @Bean
    public EntityFactory<ProductCommand, ProductEvent, Product, ProductId> productEntityFactory() {
        return forEntity(new ProductEntity());
    }

    private <Command extends ReplyType, Event, Entity, EntityId> EntityFactory<Command, Event, Entity, EntityId> forEntity(final PersistentEntity<Command, Event, Entity, EntityId> entity) {
        return new EntityFactory<>(entity)
                .withEntityMangerOptions(EntityMangerOptions.builder().snapshotBatch(1L).build())
                .withEventStore(new JpaEventStore<>(entityManager, entity.name(), jacksonSerializer))
                .withSnapshotStore(new JpaSnapshotStore<>(entity.name(), entityManager, jacksonSerializer));
    }
}
