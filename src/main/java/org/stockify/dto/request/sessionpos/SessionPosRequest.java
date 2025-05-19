package org.stockify.dto.request.sessionpos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.stockify.model.entity.SessionPosEntity}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionPosRequest {
    @NotBlank
    @PositiveOrZero
    private BigDecimal openingAmount;
    @NotBlank
    @Pattern(regexp = "^\\d{7,8}$\n", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String employeeDni;
    @NotBlank
    private LocalDateTime openingTime;

}