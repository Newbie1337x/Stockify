package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.AuditController;
import org.stockify.dto.request.audit.SaleAuditDTO;
import org.stockify.dto.request.audit.filter.PurchaseAuditFilterRequest;
import org.stockify.dto.request.audit.filter.SaleAuditFilterRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SaleAuditModelAssembler implements RepresentationModelAssembler<SaleAuditDTO, EntityModel<SaleAuditDTO>> {

    @NotNull
    @Override
    public EntityModel<SaleAuditDTO> toModel(@NotNull SaleAuditDTO saleAuditDTO) {
        return EntityModel.of(saleAuditDTO,
                linkTo(methodOn(AuditController.class)
                        .getAllPurchaseAudit(PageRequest.of(0, 10), new PurchaseAuditFilterRequest(), null))
                        .withRel("saleAudits"),
                linkTo(methodOn(AuditController.class)
                        .getAllSaleAudit(PageRequest.of(0, 10), new SaleAuditFilterRequest(), null))
                        .withRel("saleAudits")
        );
    }
}
