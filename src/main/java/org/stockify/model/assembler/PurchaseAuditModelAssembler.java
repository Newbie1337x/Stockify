package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.AuditController;
import org.stockify.dto.request.audit.PurchaseAuditDTO;
import org.stockify.dto.request.audit.filter.PurchaseAuditFilterRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PurchaseAuditModelAssembler implements RepresentationModelAssembler<PurchaseAuditDTO, EntityModel<PurchaseAuditDTO>> {

    @NotNull
    @Override
    public EntityModel<PurchaseAuditDTO> toModel(@NotNull PurchaseAuditDTO purchaseAuditDTO) {
        return EntityModel.of(purchaseAuditDTO,
                linkTo(methodOn(AuditController.class)
                        .getAllPurchaseAudit(PageRequest.of(0, 20), new PurchaseAuditFilterRequest(), null))
                        .withRel("purchaseAudits")
        );
    }
}
