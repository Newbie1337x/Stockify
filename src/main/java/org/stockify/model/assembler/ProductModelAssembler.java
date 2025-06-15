package org.stockify.model.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.product.ProductController;
import org.stockify.dto.request.product.ProductFilterRequest;
import org.stockify.dto.response.ProductResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<ProductResponse, EntityModel<ProductResponse>> {

    @NotNull
    @Override
    public EntityModel<ProductResponse> toModel(@NotNull ProductResponse productResponse) {
        return EntityModel.of(productResponse,
                linkTo(methodOn(ProductController.class)
                        .getProductById(productResponse.id()))
                        .withSelfRel(),

                linkTo(methodOn(ProductController.class)
                        .listProducts(new ProductFilterRequest(), PageRequest.of(0, 10), null))
                        .withRel("products")
        );
    }
}

