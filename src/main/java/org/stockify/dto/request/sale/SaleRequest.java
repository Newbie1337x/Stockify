package org.stockify.dto.request.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.stockify.dto.request.transaction.TransactionRequest;

@Getter
@Setter
public class SaleRequest {
    @Schema(description = "ID of the client", example = "10")
    Long clientId;

    @NotNull(message = "Transaction is required")
    @Schema(description = "Transaction details for the sale")
    TransactionRequest transaction;
}
