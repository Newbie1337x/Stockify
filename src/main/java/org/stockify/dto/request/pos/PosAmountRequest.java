package org.stockify.dto.request.pos;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.entity.PosEntity;

import java.io.Serializable;
import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosAmountRequest {
    @Schema(description = "Current amount available in the POS", example = "1500.00", required = true)
    @NotNull(message = "currentAmount must not be null")
    private BigDecimal currentAmount;
}
