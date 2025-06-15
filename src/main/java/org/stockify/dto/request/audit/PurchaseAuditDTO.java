package org.stockify.dto.request.audit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.stockify.dto.response.TransactionResponse;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "PurchaseAuditDTO", description = "Contains audit information related to a purchase transaction revision.")
public class PurchaseAuditDTO {

    @Schema(description = "Revision number of the audit entry", example = "101")
    private Long revision;

    @Schema(
            description = "Type of revision performed",
            example = "ADD",
            allowableValues = {"ADD", "MOD", "DEL"}
    )
    private String revisionType;

    @Schema(description = "Unique identifier of the audited purchase", example = "5001")
    private Long id;

    @Schema(description = "Transaction data associated with the audit", implementation = TransactionResponse.class)
    private TransactionResponse transaction;
}
