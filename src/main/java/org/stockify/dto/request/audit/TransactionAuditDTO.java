package org.stockify.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TransactionAuditDTO {
    private Long revision;            // Número de revisión
    private String revisionType;      // Tipo de cambio: ADD, MOD, DEL

    private Long id;
    private BigDecimal total;
    private LocalDateTime dateTime;
    private PaymentMethod paymentMethod;
    private String description;
    private TransactionType type;
    private Long storeId;
    private Long sessionPosId;
}
