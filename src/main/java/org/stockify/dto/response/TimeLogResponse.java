package org.stockify.dto.response;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeLogResponse {

    @Schema(description = "Unique identifier of the time log entry", example = "123")
    private Long id;

    @Schema(description = "Date of the time log", example = "2025-06-15")
    private LocalDate date;

    @Schema(description = "Time when the employee clocked in", example = "08:30:00")
    private LocalTime clockInTime;

    @Schema(description = "Time when the employee clocked out", example = "17:00:00")
    private LocalTime clockOutTime;

    @Schema(description = "Identifier of the employee associated with this time log", example = "456")
    private Long employeeId;

    @Schema(description = "Full name of the employee", example = "John Doe")
    private String employeeFullName;
}
