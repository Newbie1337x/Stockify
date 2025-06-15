package org.stockify.dto.request.transaction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/**
 * DTO for {@link org.stockify.model.entity.DetailTransactionEntity}
 */
@Value
@Getter
@Setter
@Valid
public class DetailTransactionRequest{
    @NotNull(message = "Product ID is required")
    Long productID;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be 0 or positive")
    Double quantity;
}