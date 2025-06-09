package org.stockify.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.stockify.model.entity.ProductEntity}
 */
@Value
public class ProductResponseTransaction {
    Long id;
    String name;
    BigDecimal price;
    String barcode;
}