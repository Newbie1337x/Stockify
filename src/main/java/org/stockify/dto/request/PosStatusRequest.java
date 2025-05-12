package org.stockify.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.stockify.model.enums.Status;

/**
 * DTO de petición utilizado para actualizar el estado de conexión ({@code status})
 * de un punto de venta (POS), como online u offline.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosStatusRequest {

    /**
     * Estado de conexión del punto de venta (por ejemplo, ONLINE u OFFLINE).
     * No puede ser nulo.
     */
    @NotNull(message = "status no debe ser nulo")
    private Status status;
}
