package org.stockify.dto.request.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Transaction ID is required")
    private Long transactionId;
}

