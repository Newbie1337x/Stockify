package org.stockify.dto.response.shift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import org.stockify.dto.response.EmployeeResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShiftResponse {

    @Schema(description = "Unique identifier of the shift", example = "1")
    private Long id;

    @Schema(description = "Date of the shift", example = "2025-06-15")
    private LocalDate day;

    @Schema(description = "Entry time of the shift", example = "2025-06-15T08:30:00")
    private LocalDateTime entryTime;

    @Schema(description = "Exit time of the shift", example = "2025-06-15T17:30:00")
    private LocalDateTime exitTime;

    @Schema(description = "Employee associated with the shift")
   private EmployeeResponse employee;
}

