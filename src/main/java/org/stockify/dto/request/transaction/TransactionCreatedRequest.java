package org.stockify.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.enums.PaymentMethod;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreatedRequest {

    @Schema(description = "Method used to pay the transaction", example = "CASH")
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Schema(description = "Total amount of the transaction", example = "1500.00")
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @Schema(description = "Optional description for the transaction", example = "Payment for invoice #1234")
    private String description;
}
