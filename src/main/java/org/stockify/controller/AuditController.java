package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stockify.dto.request.audit.PurchaseAuditDTO;
import org.stockify.dto.request.audit.SaleAuditDTO;
import org.stockify.dto.request.audit.TransactionAuditDTO;
import org.stockify.dto.request.audit.filter.PurchaseAuditFilterRequest;
import org.stockify.dto.request.audit.filter.SaleAuditFilterRequest;
import org.stockify.dto.request.audit.filter.TransactionAuditFilterRequest;
import org.stockify.model.assembler.PurchaseAuditModelAssembler;
import org.stockify.model.assembler.SaleAuditModelAssembler;
import org.stockify.model.assembler.TransactionAuditModelAssembler;
import org.stockify.model.service.AuditService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit")
@Tag(name = "Audits", description = "Endpoints for viewing audit records of transactions, sales, and purchases")
@SecurityRequirement(name = "bearerAuth")
public class AuditController {

    private final AuditService auditService;
    private final TransactionAuditModelAssembler transactionAuditModelAssembler;
    private final PurchaseAuditModelAssembler purchaseAuditModelAssembler;
    private final SaleAuditModelAssembler saleAuditModelAssembler;

    @Operation(
        summary = "Get all transaction audit logs with pagination and filtering",
        description = "Get a paginated list of transaction audit logs with optional filters. Returns a list with HATEOAS links."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction audit logs retrieved successfully")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    @GetMapping("/transactions")
    public ResponseEntity<PagedModel<EntityModel<TransactionAuditDTO>>> getAllTransactionAudit(
            @Parameter(hidden = true) Pageable pageable,
            @ParameterObject TransactionAuditFilterRequest filter,
            PagedResourcesAssembler<TransactionAuditDTO> assembler
    ) {
        Page<TransactionAuditDTO> page = auditService.getAllTransactionAudits(pageable, filter);
        PagedModel<EntityModel<TransactionAuditDTO>> pagedModel = assembler.toModel(page, transactionAuditModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @Operation(
        summary = "Get all purchase audit logs with pagination and filtering",
        description = "Get a paginated list of purchase audit logs with optional filters. Returns a list with HATEOAS links."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase audit logs retrieved successfully")
    })
    @GetMapping("/purchases")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<PurchaseAuditDTO>>> getAllPurchaseAudit(
            @Parameter(hidden = true) Pageable pageable,
            @ParameterObject PurchaseAuditFilterRequest filter,
            PagedResourcesAssembler<PurchaseAuditDTO> assembler
    ) {
        Page<PurchaseAuditDTO> page = auditService.getAllPurchaseAudits(pageable, filter);
        PagedModel<EntityModel<PurchaseAuditDTO>> pagedModel = assembler.toModel(page, purchaseAuditModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @Operation(
        summary = "Get all sale audit logs with pagination and filtering",
        description = "Get a paginated list of sale audit logs with optional filters. Returns a list with HATEOAS links."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale audit logs retrieved successfully")
    })
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/sales")
    public ResponseEntity<PagedModel<EntityModel<SaleAuditDTO>>> getAllSaleAudit(
            @Parameter(hidden = true) Pageable pageable,
            @ParameterObject SaleAuditFilterRequest filter,
            PagedResourcesAssembler<SaleAuditDTO> assembler
    ) {
        Page<SaleAuditDTO> page = auditService.getAllSaleAudits(pageable, filter);
        PagedModel<EntityModel<SaleAuditDTO>> pagedModel = assembler.toModel(page, saleAuditModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }
}
