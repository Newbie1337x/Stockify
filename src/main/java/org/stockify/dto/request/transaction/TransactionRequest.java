package org.stockify.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.stockify.model.entity.TransactionEntity}
 */
@Value
public class TransactionRequest{
    @NotNull
    @PositiveOrZero
    BigDecimal total;
    @NotNull
    PaymentMethod paymentMethod;
    @NotNull
    TransactionType type;
    @NotNull
    Long PosId;
    @NotNull
    Long storeId;
}