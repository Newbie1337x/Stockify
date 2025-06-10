package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleResponse {
    Long id;
    TransactionResponse transaction;
    Long dniClient;
}
