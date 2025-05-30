package org.stockify.model.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.StoreController;
import org.stockify.dto.response.StockResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StockModelAssembler implements RepresentationModelAssembler<StockResponse, EntityModel<StockResponse>> {

    @NotNull
    @Override
    public EntityModel<StockResponse> toModel(@NotNull StockResponse stockResponse) {
        return EntityModel.of(stockResponse,
                linkTo(methodOn(StoreController.class)
                        .getStock(stockResponse.product_id(), stockResponse.store_id()))
                        .withSelfRel(),
                linkTo(methodOn(StoreController.class)
                        .getStoreById(stockResponse.store_id()))
                        .withRel("store")
        );
    }
}
