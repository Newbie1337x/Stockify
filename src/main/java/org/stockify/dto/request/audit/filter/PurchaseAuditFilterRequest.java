package org.stockify.dto.request.audit.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Filter criteria for purchase audit records")
public class PurchaseAuditFilterRequest {

    @Schema(description = "Filter by revision number", example = "101")
    private Long revision;

    @Schema(
            description = "Filter by revision type",
            example = "ADD",
            allowableValues = {"ADD", "MOD", "DEL"}
    )
    private String revisionType;

    @Schema(description = "Filter by purchase ID", example = "5001")
    private Long purchaseId;

    @Schema(description = "Filter by transaction ID", example = "7003")
    private Long transactionId;

    @Schema(description = "Filter by provider ID", example = "42")
    private Long providerId;
}