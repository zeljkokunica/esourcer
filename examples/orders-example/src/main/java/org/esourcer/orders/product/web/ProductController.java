package org.esourcer.orders.product.web;

import lombok.RequiredArgsConstructor;
import org.esourcer.orders.common.Currency;
import org.esourcer.orders.common.Money;
import org.esourcer.orders.common.web.ControllerBase;
import org.esourcer.orders.product.ProductService;
import org.esourcer.orders.product.mapper.ProductMapper;
import org.esourcer.orders.product.model.ProductId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/products")
public class ProductController extends ControllerBase {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> findProducts() {
        return productService.findAllProducts().stream()
                .map(productMapper::toApi)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable("id") final String productId) {
        return productMapper.toApi(productService.getProduct(getAudit(), new ProductId(productId)));
    }

    @Secured("admin")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody final CreateProductRequest createProductRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.toApi(
                        productService.createProduct(getAudit(), createProductRequest.getName(),
                                createProductRequest.getDescription(),
                                new Money(createProductRequest.getPrice(),
                                        Currency.valueOf(createProductRequest.getCurrency())))));
    }

    @Secured("admin")
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable("id") final String productId,
            @Valid @RequestBody final CreateProductRequest updateProductRequest) {
        return productMapper
                .toApi(productService.updateProduct(
                        getAudit(),
                        new ProductId(productId),
                        updateProductRequest.getName(),
                        updateProductRequest.getDescription(),
                        new Money(updateProductRequest.getPrice(),
                                Currency.valueOf(updateProductRequest.getCurrency()))));
    }

}
