package org.stockify.dto.request.sessionpos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SessionPosFiltersRequest {

    @Schema(description = "Filter by employee ID", example = "5", nullable = true)
    private Long employeeId;

    @Schema(description = "Filter by POS ID", example = "3", nullable = true)
    private Long posId;

    @Schema(description = "Filter sessions opened from this date/time", example = "2023-06-01T08:00:00", nullable = true)
    private LocalDateTime openingTimeStart;

    @Schema(description = "Filter sessions opened until this date/time", example = "2023-06-02T00:00:00", nullable = true)
    private LocalDateTime openingTimeEnd;

    @Schema(description = "Filter sessions closed from this date/time", example = "2023-06-01T16:00:00", nullable = true)
    private LocalDateTime closeTimeStart;

    @Schema(description = "Filter sessions closed until this date/time", example = "2023-06-02T00:00:00", nullable = true)
    private LocalDateTime closeTimeEnd;

    @Schema(description = "Filter sessions with opening amount minimum", example = "500.00", nullable = true)
    private BigDecimal openingAmountMin;

    @Schema(description = "Filter sessions with opening amount maximum", example = "2000.00", nullable = true)
    private BigDecimal openingAmountMax;

    @Schema(description = "Filter sessions with closing amount minimum", example = "700.00", nullable = true)
    private BigDecimal closeAmountMin;

    @Schema(description = "Filter sessions with closing amount maximum", example = "2500.00", nullable = true)
    private BigDecimal closeAmountMax;

    @Schema(description = "Filter sessions by exact cash difference", example = "0.00", nullable = true)
    private BigDecimal cashDifference;

    @Schema(description = "Filter sessions that are currently open (true) or closed (false)", example = "true", nullable = true)
    private Boolean isOpen;
}
