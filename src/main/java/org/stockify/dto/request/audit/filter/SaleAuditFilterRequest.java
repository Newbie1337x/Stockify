package org.stockify.dto.request.audit.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Filter criteria for sale audit records")
public class SaleAuditFilterRequest {

    @Schema(description = "Filter by revision number", example = "204")
    private Long revision;

    @Schema(
            description = "Filter by revision type",
            example = "MOD",
            allowableValues = {"ADD", "MOD", "DEL"}
    )
    private String revisionType;

    @Schema(description = "Filter by sale ID", example = "3002")
    private Long saleId;

    @Schema(description = "Filter by transaction ID", example = "7003")
    private Long transactionId;

    @Schema(description = "Filter by client ID", example = "150")
    private Long clientId;
}