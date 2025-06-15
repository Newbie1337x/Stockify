package org.stockify.dto.request.shift;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShiftRequest {

    @Schema(description = "Date of the shift", example = "2025-06-15")
    @NotNull(message = "Shift day must not be null")
    private LocalDate day;

    @Schema(description = "Entry time for the shift", example = "2025-06-15T08:00:00")
    @NotNull(message = "Entry time must not be null")
    private LocalDateTime entryTime;

    @Schema(description = "Exit time for the shift", example = "2025-06-15T16:00:00")
    @NotNull(message = "Exit time must not be null")
    private LocalDateTime exitTime;

    @Schema(description = "List of employee IDs assigned to the shift", example = "[1, 2, 3]", nullable = true)
    private List<Long> employeeIds;
}
