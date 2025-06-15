package org.stockify.dto.request.shift;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ShiftFilterRequest {

    @Schema(description = "Filter shifts starting from this day (inclusive)", example = "2025-06-01", nullable = true)
    private LocalDate dayFrom;

    @Schema(description = "Filter shifts ending until this day (inclusive)", example = "2025-06-30", nullable = true)
    private LocalDate dayTo;

    @Schema(description = "Filter shifts with entry time from this date/time (inclusive)", example = "2025-06-01T08:00:00", nullable = true)
    private LocalDateTime entryTimeFrom;

    @Schema(description = "Filter shifts with entry time until this date/time (inclusive)", example = "2025-06-01T18:00:00", nullable = true)
    private LocalDateTime entryTimeTo;

    @Schema(description = "Filter shifts with exit time from this date/time (inclusive)", example = "2025-06-01T16:00:00", nullable = true)
    private LocalDateTime exitTimeFrom;

    @Schema(description = "Filter shifts with exit time until this date/time (inclusive)", example = "2025-06-01T23:00:00", nullable = true)
    private LocalDateTime exitTimeTo;

    @Schema(description = "Filter shifts by list of employee IDs", example = "[1, 2, 5]", nullable = true)
    private List<Long> employeesIds;
}
