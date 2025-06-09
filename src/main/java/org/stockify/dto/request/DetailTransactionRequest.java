package org.stockify.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.stockify.model.entity.DetailTransactionEntity}
 */
@Value
@Getter
@Setter
public class DetailTransactionRequest{
    @NotNull(message = "Product ID is required")
    Long productID;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be 0 or positive")
    Double quantity;
}