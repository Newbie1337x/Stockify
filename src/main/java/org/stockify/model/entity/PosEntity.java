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
@Builder
@Entity
@Table(name = "pos")
public class PosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_amount")
    private BigDecimal currentAmount;

    @OneToMany(mappedBy = "posEntity", fetch = FetchType.LAZY)
    private Set<SessionPosEntity> sessionPosEntities;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;


}
