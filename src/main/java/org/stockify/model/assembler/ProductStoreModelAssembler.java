package org.stockify.model.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.product.ProductController;
import org.stockify.controller.store.StoreStockController;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.response.ProductStoreResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductStoreModelAssembler implements RepresentationModelAssembler<ProductStoreResponse, EntityModel<ProductStoreResponse>> {

    @NotNull
    @Override
    public EntityModel<ProductStoreResponse> toModel(@NotNull ProductStoreResponse productStoreResponse) {
        return EntityModel.of(productStoreResponse,
                linkTo(methodOn(ProductController.class)
                        .getProductById(productStoreResponse.productID()))
                        .withSelfRel(),
                linkTo(methodOn(ProductController.class)
                        .listProducts(new ProductFilterRequest(), PageRequest.of(0, 10), null))
                        .withRel("products"),
                linkTo(methodOn(StoreStockController.class)
                        .getStock(productStoreResponse.storeID(), productStoreResponse.productID()))
                        .withRel("stock")
        );
    }
}