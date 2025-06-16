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
public class SessionPosResponse {

    @Schema(description = "Unique identifier of the POS session", example = "12345")
    private Long id;

    @Schema(description = "Date and time when the POS session was opened", example = "2025-06-15T08:30:00")
    private LocalDateTime openingTime;

    @Schema(description = "Date and time when the POS session was closed", example = "2025-06-15T16:45:00")
    private LocalDateTime closeTime;

    @Schema(description = "Initial amount of money at POS opening", example = "150.00")
    private BigDecimal openingAmount;

    @Schema(description = "Amount of money counted at POS closing", example = "500.00")
    private BigDecimal closeAmount;

    @Schema(description = "Expected amount of money according to sales and transactions", example = "480.00")
    private BigDecimal expectedAmount;

    @Schema(description = "Difference between counted and expected cash amount", example = "20.00")
    private BigDecimal cashDifference;

    @Schema(description = "Employee who opened and managed the POS session")
    private EmployeeResponse employee;

    @Schema(description = "Identifier of the POS point", example = "10")
    private Long idPos;

}
