package org.stockify.dto.response;

import lombok.*;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreatedResponse {
    Long id;
    BigDecimal total;
    LocalDateTime dateTime;
    PaymentMethod paymentMethod;
    String description;
    TransactionType type;
    Long sessionPosId;
    Long employeeId;
    String employeeDni;
    Long idPos;
    Long storeId;
    String storeName;
}
