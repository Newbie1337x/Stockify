package org.stockify.dto.request.shift;

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
    private LocalDate dayFrom;
    private LocalDate dayTo;

    private LocalDateTime entryTimeFrom;
    private LocalDateTime entryTimeTo;

    private LocalDateTime exitTimeFrom;
    private LocalDateTime exitTimeTo;

    private List<Long> employeesId;
}
