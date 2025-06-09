package org.stockify.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;
import org.stockify.dto.request.DetailTransactionRequest;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link org.stockify.model.entity.TransactionEntity}
 */
@Value
public class TransactionRequest{
    @NotNull
    PaymentMethod paymentMethod;
    List<DetailTransactionRequest> detailTransactions;

}