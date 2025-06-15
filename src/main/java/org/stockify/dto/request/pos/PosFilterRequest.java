package org.stockify.dto.request.pos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.stockify.model.enums.Status;

import java.math.BigDecimal;

@Getter
@Setter
public class PosFilterRequest {

    @Schema(description = "Filter by POS ID", example = "1", nullable = true)
    private Long id;

    @Schema(description = "Filter by Store ID", example = "2", nullable = true)
    private Long storeId;

    @Schema(description = "Filter by POS status", example = "ACTIVE", nullable = true)
    private Status status;

    @Schema(description = "Filter by Employee ID", example = "3", nullable = true)
    private Long employeeId;

    @Schema(description = "Filter by POS current amount", example = "1000", nullable = true)
    private BigDecimal currentAmount;
}
