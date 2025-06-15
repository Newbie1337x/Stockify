package org.stockify.dto.request.sessionpos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionPosRequest {

    @Schema(description = "Initial amount when opening the POS session, must be zero or positive.", example = "1000.00")
    @NotNull(message = "Opening amount must not be null")
    @PositiveOrZero(message = "Opening amount must be zero or positive")
    private BigDecimal openingAmount;

    @Schema(description = "Employee DNI (National Identity Document), digits only, length 7 to 8.", example = "12345678")
    @NotBlank(message = "Employee DNI must not be blank")
    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String employeeDni;
}
