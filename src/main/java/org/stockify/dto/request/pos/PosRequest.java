package org.stockify.dto.request.pos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;

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

    @NotNull(message = "currentAmount must not be null")
    private BigDecimal currentAmount;

    @NotNull(message = "storeId must not be null")
    private Long storeId;

    @NotNull(message = "status must not be null")
    private Status status;

    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String employeeDni;
    }
