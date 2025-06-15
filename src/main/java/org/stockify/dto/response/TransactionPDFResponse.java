package org.stockify.dto.response;

import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Builder
@Getter
public class TransactionPDFResponse {

    @Schema(description = "File path of the generated PDF", example = "/files/transaction_123.pdf")
    private String path;

    @Schema(description = "Details of the transaction included in the PDF")
    private TransactionResponse transaction;
}
