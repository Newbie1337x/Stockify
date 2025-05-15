package org.stockify.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeLogResponse {
    private Long id;
    private LocalDate date;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;

    private Long employeeId;
    private String employeeFullName;
}
