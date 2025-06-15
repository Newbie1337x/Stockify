package org.stockify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosResponse {

    @Schema(description = "Unique identifier of the POS", example = "1001")
    private Long id;

    @Schema(description = "Current cash amount in the POS", example = "2500.75")
    private BigDecimal currentAmount;

    @Schema(description = "Current status of the POS", example = "OPEN")
    private Status status;

    @Schema(description = "Identifier of the associated store", example = "10")
    private Long idStore;
}
