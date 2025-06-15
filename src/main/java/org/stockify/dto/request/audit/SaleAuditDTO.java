package org.stockify.dto.request.audit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.stockify.dto.response.TransactionResponse;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "SaleAuditDTO", description = "Contains audit information related to a sale transaction revision.")
public class SaleAuditDTO {

    @Schema(description = "Revision number of the audit entry", example = "204")
    private Long revision;

    @Schema(
            description = "Type of revision performed",
            example = "MOD",
            allowableValues = {"ADD", "MOD", "DEL"}
    )
    private String revisionType;

    @Schema(description = "Unique identifier of the audited sale", example = "3002")
    private Long id;

    @Schema(description = "Transaction data associated with the audit", implementation = TransactionResponse.class)
    private TransactionResponse transaction;

    @Schema(description = "Identifier of the client involved in the transaction", example = "150")
    private Long clientId;
}
