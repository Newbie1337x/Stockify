package org.stockify.dto.request.pos;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.entity.PosEntity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * DTO de petición utilizado para actualizar el monto actual ({@code currentAmount})
 * de una entidad {@link PosEntity}.
 * Representa el saldo disponible en un punto de venta (POS), con validaciones que
 * aseguran que el valor no sea nulo ni negativo.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosAmountRequest {
    @NotNull(message = "currentAmount must not be null")
    private BigDecimal currentAmount;
}
