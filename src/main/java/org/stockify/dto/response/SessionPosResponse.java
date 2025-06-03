package org.stockify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.entity.PosEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.stockify.model.entity.SessionPosEntity}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionPosResponse {
    private Long id;
    private LocalDateTime openingTime;
    private LocalDateTime closeTime;
    private BigDecimal openingAmount;
    private BigDecimal closeAmount;
    private BigDecimal expectedAmount;
    private BigDecimal cashDifference;
    private EmployeeResponse employee;
    private Long idPos;
}