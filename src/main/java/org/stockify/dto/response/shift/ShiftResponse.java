package org.stockify.dto.response.shift;

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
public class ShiftResponse {
    private Long id;
    private LocalDate day;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private List<Long> employeeIds;
}
