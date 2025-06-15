package org.stockify.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @Schema(description = "Payment method used in the transaction", example = "CASH")
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Schema(description = "List of transaction details")
    @NotNull(message = "Details are required")
    private List<DetailTransactionRequest> detailTransactions;

    @Schema(description = "Optional description of the transaction", example = "Customer paid in cash")
    private String description;
}
