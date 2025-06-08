package org.stockify.dto.request.purchase;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseFilterRequest {

    private Long providerId;
    private Long purchaseId;
    private Long transactionId;
}
