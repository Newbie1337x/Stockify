package org.stockify.controller.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.service.PurchaseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/pos/{posID}/transactions/purchases")
@Tag(name = "Purchases", description = "Endpoints for managing purchase transactions")
public class TransactionPurchaseController {

    private final PurchaseService purchaseService;

    @Operation(summary = "Create a purchase transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Store or POS not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    @Transactional
    public ResponseEntity<PurchaseResponse> create(
            @Parameter(description = "Purchase request body") @Valid @RequestBody PurchaseRequest request,
            @Parameter(description = "ID of the POS") @PathVariable Long posID) {

        PurchaseResponse response = purchaseService.createPurchase(request,posID);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
