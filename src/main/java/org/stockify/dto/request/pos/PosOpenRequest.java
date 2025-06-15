package org.stockify.dto.request.pos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PosOpenRequest {

    @Schema(description = "Initial amount to open the POS cash register", example = "1000.00")
    @NotNull(message = "currentAmount must not be null")
    private BigDecimal currentAmount;

    @Schema(description = "ID of the store where the POS is opened", example = "1")
    @NotNull(message = "id_store must not be null")
    private Long id_store;

    @Schema(description = "Employee's DNI who opens the cash register, digits only, 7 to 8 characters", example = "12345678")
    @NotNull(message = "employeeDni must not be null")
    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String employeeDni;
}

