package org.stockify.dto.request.shift;

import jakarta.validation.constraints.NotNull;
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
public class ShiftRequest {
    @NotNull
    private LocalDate day;

    @NotNull
    private LocalDateTime entryTime;

    @NotNull
    private LocalDateTime exitTime;

    private List<Long> employeeIds;
}
