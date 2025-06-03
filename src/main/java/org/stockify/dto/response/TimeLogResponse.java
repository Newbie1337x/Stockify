package org.stockify.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeLogResponse {
    private Long id;
    private LocalDate date;
    private LocalTime clockInTime;
    private LocalTime clockOutTime;

    private Long employeeId;
    private String employeeFullName;
}
