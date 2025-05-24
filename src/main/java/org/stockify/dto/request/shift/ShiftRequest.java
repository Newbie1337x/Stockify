package org.stockify.dto.request.shift;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShiftRequest {
    @NotBlank
    private LocalDate day;

    @NotBlank
    private LocalDateTime entryTime;

    @NotBlank
    private LocalDateTime exitTime;
}