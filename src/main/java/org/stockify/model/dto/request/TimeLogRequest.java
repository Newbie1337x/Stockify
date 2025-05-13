package org.stockify.model.dto.request;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime clockInTime;

    @NotNull
    private LocalDateTime clockOutTime;
}
