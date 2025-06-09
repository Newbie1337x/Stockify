package org.stockify.dto.response;

import lombok.Value;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link org.stockify.model.entity.TransactionEntity}
 */
@Value
public class TransactionResponse{
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
    Set<DetailTransactionResponse> detailTransactions;
}