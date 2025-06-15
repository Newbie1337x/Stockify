package org.stockify.dto.request.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.dto.request.transaction.TransactionRequest;
@Getter
@Setter
@NoArgsConstructor
public class SaleRequestnig {
    @Schema(description = "ID of the client", example = "10")
    Long clientId;

    @NotNull(message = "Transaction is required")
    @Valid
    @Schema(description = "Transaction details for the sale")
    TransactionRequest transaction;
}
