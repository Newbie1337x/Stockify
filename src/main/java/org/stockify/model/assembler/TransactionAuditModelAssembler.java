package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.AuditController;
import org.stockify.dto.request.audit.TransactionAuditDTO;
import org.stockify.dto.request.audit.filter.TransactionAuditFilterRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionAuditModelAssembler implements RepresentationModelAssembler<TransactionAuditDTO, EntityModel<TransactionAuditDTO>> {

    @NotNull
    @Override
    public EntityModel<TransactionAuditDTO> toModel(@NotNull TransactionAuditDTO transactionAuditDTO) {
        return EntityModel.of(transactionAuditDTO,
                linkTo(methodOn(AuditController.class)
                        .getAllTransactionAudit(PageRequest.of(0, 20), new TransactionAuditFilterRequest(), null))
                        .withRel("transactionAudits")
        );
    }
}
