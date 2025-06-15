package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class PurchaseResponse {

    @Schema(description = "Unique identifier of the purchase", example = "1001")
    private Long id;

    @Schema(description = "Associated transaction details")
    private TransactionResponse transaction;

    @Schema(description = "Provider details for this purchase")
    private ProviderResponse provider;

}
