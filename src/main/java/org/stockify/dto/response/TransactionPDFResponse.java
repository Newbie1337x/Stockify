package org.stockify.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionPDFResponse {
    String path;
    TransactionResponse transaction;
}
