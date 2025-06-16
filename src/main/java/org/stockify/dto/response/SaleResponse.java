package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleResponse {
    @Schema(description = "Unique identifier of the sale", example = "1")
    Long id;

    @Schema(description = "Unique identifier of the client", example = "10")
    Long clientId;

    @Schema(description = "Transaction details of the sale")
    TransactionResponse transaction;

    @Schema(description = "Client's DNI number", example = "12345678")
    Long clientDni;
}
