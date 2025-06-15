package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stockify.dto.request.audit.PurchaseAuditDTO;
import org.stockify.dto.request.audit.SaleAuditDTO;
import org.stockify.dto.request.audit.TransactionAuditDTO;
import org.stockify.model.service.AuditService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit")
@Tag(name = "Audits", description = "Endpoints for viewing audit records of transactions, sales, and purchases")
public class AuditController {

    private final AuditService auditService;

    @Operation(summary = "Get all transaction audit logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction audit logs retrieved successfully")
    })
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionAuditDTO>> getAllTransactionAudit() {
        return ResponseEntity.ok(auditService.getAllTransactionAudits());
    }

    @Operation(summary = "Get all purchase audit logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase audit logs retrieved successfully")
    })
    @GetMapping("/purchases")
    public ResponseEntity<List<PurchaseAuditDTO>> getAllPurchaseAudit() {
        return ResponseEntity.ok(auditService.getAllPurchaseAudits());
    }

    @Operation(summary = "Get all sale audit logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale audit logs retrieved successfully")
    })
    @GetMapping("/sales")
    public ResponseEntity<List<SaleAuditDTO>> getAllSaleAudit() {
        return ResponseEntity.ok(auditService.getAllSaleAudits());
    }
}
