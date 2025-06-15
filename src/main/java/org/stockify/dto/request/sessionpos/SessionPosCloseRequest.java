package org.stockify.dto.request.sessionpos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionPosCloseRequest {

    @Schema(description = "Closing amount of the POS session. Must be zero or positive.", example = "1500.00")
    @NotNull(message = "Close amount must not be null")
    @PositiveOrZero(message = "Close amount must be zero or positive")
    private BigDecimal closeAmount;
}
