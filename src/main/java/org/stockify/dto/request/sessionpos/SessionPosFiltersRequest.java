package org.stockify.dto.request.sessionpos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SessionPosFiltersRequest {
    private Long employeeId;
    private Long posId;
    private LocalDateTime openingTimeStart;
    private LocalDateTime openingTimeEnd;
    private LocalDateTime closeTimeStart;
    private LocalDateTime closeTimeEnd;
    private BigDecimal openingAmountMin;
    private BigDecimal openingAmountMax;
    private BigDecimal closeAmountMin;
    private BigDecimal closeAmountMax;
    private Boolean isOpen;
}
