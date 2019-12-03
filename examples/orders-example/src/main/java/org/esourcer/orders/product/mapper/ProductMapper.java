package org.esourcer.orders.product.mapper;

import org.esourcer.orders.product.model.Product;
import org.esourcer.orders.product.web.ProductDetailsDto;
import org.esourcer.orders.product.web.ProductDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toApi(final Product model) {
        return new ProductDto(
                model.getId().getId(),
                model.getName(),
                model.getDescription(),
                model.getPrice().getAmount(),
                model.getPrice().getCurrency().name(),
                model.getVersion(),
                model.getCreated().getInitiatedAt(),
                model.getVersions().stream().map(this::toApiDetails).collect(Collectors.toList()));
    }

    public ProductDetailsDto toApiDetails(final Product model) {
        return new ProductDetailsDto(
                model.getId().getId(),
                model.getName(),
                model.getDescription(),
                model.getPrice().getAmount(),
                model.getPrice().getCurrency().name(),
                model.getVersion(),
                model.getCreated().getInitiatedAt());
    }

}
