package org.stockify.model.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.SaleController;
import org.stockify.dto.request.sale.SaleFilterRequest;
import org.stockify.dto.response.SaleResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SaleModelAssembler implements RepresentationModelAssembler<SaleResponse, EntityModel<SaleResponse>> {
    @Override
    public EntityModel<SaleResponse> toModel(SaleResponse saleResponse) {
        return EntityModel.of(saleResponse,
                linkTo(methodOn(SaleController.class).getSaleById(saleResponse.getId())).withSelfRel(),
                linkTo(methodOn(SaleController.class).getAll(new SaleFilterRequest(),0, 20, null)).withRel("clients"),
                linkTo(methodOn(SaleController.class).deleteSaleById(saleResponse.getId())).withRel("delete"),
                linkTo(methodOn(SaleController.class).putSale(saleResponse.getId(), null)).withRel("update"),
                linkTo(methodOn(SaleController.class).patchSale(saleResponse.getId(), null)).withRel("partial-update"));
    }
}
