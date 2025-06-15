package org.stockify.dto.request.purchase;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.stockify.dto.request.transaction.TransactionRequest;

@Getter
@Setter
public class PurchaseRequest {

    @Schema(description = "Identifier of the provider", example = "1")
    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @Schema(description = "Transaction details associated with the purchase")
    @NotNull(message = "Transaction is required")
    private TransactionRequest transaction;
}
