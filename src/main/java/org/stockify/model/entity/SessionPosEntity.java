package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "session_pos")
public class SessionPosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "opening_time",insertable = false, updatable = false, nullable = false)
    private LocalDateTime openingTime;

    @Column(name = "close_time", updatable = false)
    private LocalDateTime closeTime;

    @Column(name = "opening_amount", nullable = false)
    private BigDecimal openingAmount;

    @Column(name = "close_amount")
    private BigDecimal closeAmount;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @ManyToOne()
    @JoinColumn(name = "pos_id")
    private PosEntity posEntity;

}
