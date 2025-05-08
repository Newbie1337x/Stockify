package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "pos")
public class PosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "opening_time",insertable = false, updatable = false, nullable = false)
    private LocalDateTime openingTime;

    @Column(name = "close_time", updatable = false)
    private LocalDateTime closeTime;

    @Column(name = "opening_amount", nullable = false)
    private Double openingAmount;

    @Column(name = "current_amount")
    private Double currentAmount;

    @ManyToOne()
    @JoinColumn(name = "shift_id")
    private ShiftEntity shiftEntity;




}
