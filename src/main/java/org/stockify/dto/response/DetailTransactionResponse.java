package org.stockify.dto.response;

import jakarta.validation.Valid;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@AllArgsConstructor
@Value
@Builder
@Getter
@Setter
public class DetailTransactionResponse {

    @Schema(description = "Unique identifier of the detail transaction", example = "1")
    Long id;

    @Schema(description = "Quantity of the product in this transaction detail", example = "3.5")
    BigDecimal quantity;

    @Schema(description = "Subtotal amount for this detail line", example = "150.75")
    BigDecimal subtotal;

    @Valid
    @Schema(description = "Product information associated with this detail transaction")
    ProductResponseTransaction product;
}
