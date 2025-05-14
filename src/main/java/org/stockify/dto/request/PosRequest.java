package org.stockify.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * DTO for {@link org.stockify.model.entity.PosEntity}
 * DTO de petición que representa los datos necesarios para crear o actualizar una entidad {@link PosEntity}.
 * Se utiliza para recibir información del cliente antes de ser procesada y almacenada en la base de datos.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosRequest{
    @NotNull(message = "currentAmount no debe ser nulo")
    @PositiveOrZero(message = "currentAmount debe ser positivo o cero")
    private BigDecimal currentAmount;

    @NotNull(message = "status no debe ser nulo")
    private Status status;
}