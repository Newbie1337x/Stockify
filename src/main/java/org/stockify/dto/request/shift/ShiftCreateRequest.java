package org.stockify.dto.request.shift;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftCreateRequest {

    @Schema(description = "Date of the shift", example = "2025-06-15")
    @NotNull(message = "Shift day must not be null")
    private LocalDate day;

    @Schema(description = "Entry time for the shift", example = "2025-06-15T08:00:00")
    @NotNull(message = "Entry time must not be null")
    private LocalDateTime entryTime;

    @Schema(description = "Exit time for the shift", example = "2025-06-15T16:00:00")
    @NotNull(message = "Exit time must not be null")
    private LocalDateTime exitTime;

    private String employeeDni;

}


