package org.stockify.controller.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionPDFResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.service.TransactionService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operations related to generic transactions and PDF generation")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create a generic transaction (type = OTHER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Store or POS not found")
    })
    @PostMapping("/stores/{storeID}/pos/{posID}/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the POS") @PathVariable Long posID,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse response = transactionService.createTransaction(
                request, storeID, posID, TransactionType.OTHER
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate PDF for a transaction by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "PDF generation error")
    })
    @GetMapping("/transaction/pdf/{idTransaction}")
    public ResponseEntity<EntityModel<TransactionPDFResponse>> generatePdf(
            @Parameter(description = "ID of the transaction") @PathVariable Long idTransaction) throws Exception {

        return transactionService.generatePdf(idTransaction);
    }
}
