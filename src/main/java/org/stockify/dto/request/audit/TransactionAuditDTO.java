package org.stockify.dto.request.audit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "TransactionAuditDTO", description = "Contains audit information for a generic transaction.")
public class TransactionAuditDTO {

    @Schema(description = "Revision number of the audit entry", example = "309")
    private Long revision;

    @Schema(
            description = "Type of revision performed",
            example = "DEL",
            allowableValues = {"ADD", "MOD", "DEL"}
    )
    private String revisionType;

    @Schema(description = "Unique identifier of the transaction", example = "7003")
    private Long id;

    @Schema(description = "Total amount of the transaction", example = "3499.99")
    private BigDecimal total;

    @Schema(description = "Date and time when the transaction occurred", example = "2025-06-14T15:30:00")
    private LocalDateTime dateTime;

    @Schema(
            description = "Payment method used in the transaction",
            example = "CASH",
            implementation = PaymentMethod.class
    )
    private PaymentMethod paymentMethod;

    @Schema(description = "Description or notes related to the transaction", example = "Purchase of electronics and accessories")
    private String description;

    @Schema(
            description = "Type of transaction (e.g., SALE, PURCHASE)",
            example = "SALE",
            implementation = TransactionType.class
    )
    private TransactionType type;

    @Schema(description = "ID of the store where the transaction was made", example = "3")
    private Long storeId;

    @Schema(description = "ID of the POS session associated with the transaction", example = "18")
    private Long sessionPosId;
}
