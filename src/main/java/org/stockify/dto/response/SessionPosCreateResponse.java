package org.stockify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionPosCreateResponse {

    @Schema(description = "Unique identifier of the POS session", example = "12345")
    private Long id;

    @Schema(description = "Date and time when the POS session was opened", example = "2025-06-15T08:30:00")
    private LocalDateTime openingTime;

    @Schema(description = "Initial amount of money available at POS opening", example = "150.00")
    private BigDecimal openingAmount;

    @Schema(description = "Identifier of the POS where the session was opened", example = "10")
    private Long idPos;

    @Schema(description = "Employee who opened the POS session")
    private EmployeeResponse employee;
}
