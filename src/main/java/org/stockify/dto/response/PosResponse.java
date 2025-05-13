package org.stockify.dto.response;

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
 * DTO de respuesta que representa los datos enviados desde el servidor al cliente.
 * Se utiliza para devolver información estructurada después de procesar una solicitud.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
    public class PosResponse implements Serializable{
    private Long id;
    private BigDecimal currentAmount;
    private Status status;
}