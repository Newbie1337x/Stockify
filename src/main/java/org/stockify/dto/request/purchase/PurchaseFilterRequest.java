package org.stockify.dto.request.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseFilterRequest {

    @Schema(description = "Identifier of the provider", example = "1")
    private Long providerId;

    @Schema(description = "Identifier of the purchase", example = "100")
    private Long purchaseId;

    @Schema(description = "Identifier of the transaction", example = "200")
    private Long transactionId;
}
