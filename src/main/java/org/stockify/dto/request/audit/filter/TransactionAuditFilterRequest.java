package org.stockify.dto.request.audit.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Filter criteria for transaction audit records")
public class TransactionAuditFilterRequest {

    @Schema(description = "Filter by revision number", example = "309")
    private Long revision;

    @Schema(
            description = "Filter by revision type",
            example = "ADD",
            allowableValues = {"ADD", "MOD", "DEL"}
    )
    private String revisionType;

    @Schema(description = "Filter by transaction ID", example = "7003")
    private Long transactionId;

    @Schema(description = "Filter by payment method", implementation = PaymentMethod.class)
    private PaymentMethod paymentMethod;

    @Schema(description = "Filter by transaction type", implementation = TransactionType.class)
    private TransactionType type;

    @Schema(description = "Filter by store ID", example = "3")
    private Long storeId;

    @Schema(description = "Filter by session POS ID", example = "18")
    private Long sessionPosId;

    @Schema(description = "Filter by transactions after this date", example = "2025-01-01T00:00:00")
    private LocalDateTime fromDate;

    @Schema(description = "Filter by transactions before this date", example = "2025-12-31T23:59:59")
    private LocalDateTime toDate;
}