package org.stockify.dto.request.pos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.entity.PosEntity;

import java.math.BigDecimal;
/**
 * DTO for {@link org.stockify.model.entity.PosEntity}
 * DTO de petición que representa los datos necesarios incializar a {@link PosEntity}.
 * Se utiliza para recibir información del cliente antes de ser procesada y almacenada en la base de datos.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosOpenRequest {
    @NotNull(message = "currentAmount must not be null")
    private BigDecimal currentAmount;

    private Long id_store;
    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String employeeDni;
}
