package org.stockify.dto.request.sessionpos;

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
    @NotNull
    @PositiveOrZero
    private BigDecimal closeAmount;
}