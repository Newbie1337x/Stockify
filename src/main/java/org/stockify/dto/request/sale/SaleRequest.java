package org.stockify.dto.request.sale;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.stockify.dto.request.transaction.TransactionRequest;

@Getter
@Setter
public class SaleRequest {
    Long clientId;

    @NotNull(message = "Transaction is required")
    TransactionRequest transaction;
}
