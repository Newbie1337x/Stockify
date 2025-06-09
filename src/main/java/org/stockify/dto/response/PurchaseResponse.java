package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseResponse {

    private Long id;
   // private TransactionResponse transaction; TODO: Uncomment when TransactionResponse is available
    private ProviderResponse provider;

}
