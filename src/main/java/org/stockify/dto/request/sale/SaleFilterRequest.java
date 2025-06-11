package org.stockify.dto.request.sale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleFilterRequest {
    private Long saleId;
    private Long clientId;
    private Long transactionId;
}
