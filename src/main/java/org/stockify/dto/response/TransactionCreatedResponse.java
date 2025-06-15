package org.stockify.dto.response;

import lombok.*;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreatedResponse {

    @Schema(description = "Unique identifier of the transaction", example = "1001")
    private Long id;

    @Schema(description = "Total amount of the transaction", example = "150.75")
    private BigDecimal total;

    @Schema(description = "Date and time when the transaction was made", example = "2025-06-15T14:30:00")
    private LocalDateTime dateTime;

    @Schema(description = "Payment method used in the transaction", example = "CASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Description or notes about the transaction", example = "Payment for order #123")
    private String description;

    @Schema(description = "Type of the transaction", example = "SALE")
    private TransactionType type;

    @Schema(description = "Identifier of the POS session related to this transaction", example = "45")
    private Long sessionPosId;

    @Schema(description = "Identifier of the employee who performed the transaction", example = "789")
    private Long employeeId;

    @Schema(description = "DNI of the employee", example = "12345678")
    private String employeeDni;

    @Schema(description = "Identifier of the POS where the transaction occurred", example = "10")
    private Long idPos;

    @Schema(description = "Identifier of the store where the transaction occurred", example = "5")
    private Long storeId;

    @Schema(description = "Name of the store where the transaction occurred", example = "Main Street Store")
    private String storeName;
}
