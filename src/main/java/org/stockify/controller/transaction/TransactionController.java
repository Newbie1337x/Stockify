package org.stockify.controller.transaction;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.transaction.TransactionCreatedRequest;
import org.stockify.dto.response.TransactionCreatedResponse;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.service.TransactionService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operations related to generic transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create a generic transaction (type = OTHER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Store or POS not found")
    })
    @PostMapping("/stores/{storeID}/pos/{posID}/transactions")
    public ResponseEntity<TransactionCreatedResponse> createTransaction(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the POS") @PathVariable Long posID,
             @RequestBody @Valid TransactionCreatedRequest request) {
        return ResponseEntity.ok(transactionService.saveTransaction(
                request, storeID, posID, TransactionType.OTHER));
    }

}