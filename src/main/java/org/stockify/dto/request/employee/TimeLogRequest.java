package org.stockify.dto.request.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "TimeLogRequest", description = "Request body to log employee working hours for a specific date.")
public class TimeLogRequest {

    @NotNull
    @Schema(description = "Identifier of the employee", example = "123")
    private Long employeeId;

    @NotNull
    @Schema(description = "Date of the time log entry", example = "2025-06-15", type = "string", format = "date")
    private LocalDate date;

    @NotNull
    @Schema(description = "Clock-in time of the employee", example = "08:30:00", type = "string", format = "time")
    private LocalTime clockInTime;

    @NotNull
    @Schema(description = "Clock-out time of the employee", example = "17:45:00", type = "string", format = "time")
    private LocalTime clockOutTime;
}
