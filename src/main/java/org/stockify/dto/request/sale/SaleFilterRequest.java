package org.stockify.dto.request.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleFilterRequest {
    @Schema(description = "Filter by sale ID", example = "1")
    private Long saleId;

    @Schema(description = "Filter by client ID", example = "10")
    private Long clientId;

    @Schema(description = "Filter by transaction ID", example = "100")
    private Long transactionId;
}
