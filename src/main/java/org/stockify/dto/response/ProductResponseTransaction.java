package org.stockify.dto.response;

import lombok.Value;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
@Value
public class ProductResponseTransaction {

    @Schema(description = "Unique identifier of the product", example = "123")
    Long id;

    @Schema(description = "Name of the product", example = "Wireless Mouse")
    String name;

    @Schema(description = "Price of the product", example = "29.99")
    BigDecimal price;

    @Schema(description = "Barcode of the product", example = "1234567890123")
    String barcode;
}
