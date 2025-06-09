package org.stockify.dto.request;

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
    String productBarcode;
    BigDecimal quantity;
}