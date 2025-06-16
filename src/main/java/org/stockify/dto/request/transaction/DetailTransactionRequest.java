package org.stockify.dto.request.transaction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for {@link org.stockify.model.entity.DetailTransactionEntity}
 */
@Value
@Getter
@Setter
@Valid
public class DetailTransactionRequest {
    @Schema(description = "ID of the product", example = "123")
    @NotNull(message = "Product ID is required")
    Long productID;

    @Schema(description = "Quantity of the product in the transaction (must be 0 or positive)", example = "10")
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be 0 or positive")
    Double quantity;
}
