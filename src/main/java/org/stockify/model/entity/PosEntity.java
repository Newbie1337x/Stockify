package org.stockify.model.entity;
import jakarta.persistence.*;
import lombok.*;
import org.stockify.model.enums.Status;

import java.math.BigDecimal;
import java.util.Set;

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

    @Column(name = "current_amount")
    private BigDecimal currentAmount;

    @OneToMany(mappedBy = "posEntity")
    private Set<SessionPosEntity> sessionPosEntities;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;


}
