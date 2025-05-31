package org.stockify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for the response when a POS session is created (only opening data).
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionPosCreateResponse {
    private Long id;
    private LocalDateTime openingTime;
    private BigDecimal openingAmount;
    private Long idPos;
    private EmployeeResponse employee;
}
