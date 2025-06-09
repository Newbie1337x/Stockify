package org.stockify.dto.request.purchase;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.stockify.dto.request.DetailTransactionRequest;
import org.stockify.dto.request.transaction.TransactionRequest;

import java.util.List;

@Getter
@Setter
public class PurchaseRequest {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotEmpty(message = "Transaction is required")
    private TransactionRequest transaction;
}

