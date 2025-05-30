package org.stockify.model.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.StoreController;
import org.stockify.dto.response.StoreResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StoreModelAssembler implements RepresentationModelAssembler<StoreResponse, EntityModel<StoreResponse>> {

    @NotNull
    @Override
    public EntityModel<StoreResponse> toModel(@NotNull StoreResponse storeResponse) {
        return EntityModel.of(storeResponse,
                linkTo(methodOn(StoreController.class)
                        .getStoreById(storeResponse.id()))
                        .withSelfRel(),
                linkTo(methodOn(StoreController.class)
                        .getAllStores(PageRequest.of(0, 10), null))
                        .withRel("stores"),
                linkTo(methodOn(StoreController.class)
                        .getProductsFromStore(storeResponse.id(), PageRequest.of(0, 10), null, null))
                        .withRel("products")
        );
    }
}
