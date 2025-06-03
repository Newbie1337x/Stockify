package org.stockify.dto.request.employee;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeLogRequest implements Serializable {
    @NotNull
    private Long employeeId;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime clockInTime;

    @NotNull
    private LocalTime clockOutTime;
}
