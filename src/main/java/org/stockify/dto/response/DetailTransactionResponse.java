package org.stockify.dto.response;

import jakarta.validation.Valid;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Value
@Builder
@Setter
@Getter
public class DetailTransactionResponse {
    Long id;
    BigDecimal quantity;
    BigDecimal subtotal;
    ProductResponseTransaction product;
}