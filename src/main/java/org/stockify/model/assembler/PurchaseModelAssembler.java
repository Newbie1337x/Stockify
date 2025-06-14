package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.PurchaseController;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.response.PurchaseResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PurchaseModelAssembler implements RepresentationModelAssembler<PurchaseResponse, EntityModel<PurchaseResponse>> {

    @NotNull
    @Override
    public EntityModel<PurchaseResponse> toModel(@NotNull PurchaseResponse purchaseResponse) {
        return EntityModel.of(purchaseResponse,
                linkTo(methodOn(PurchaseController.class)
                        .list(PageRequest.of(0, 10), new PurchaseFilterRequest(), null))
                        .withRel("purchases")
        );
    }
}
