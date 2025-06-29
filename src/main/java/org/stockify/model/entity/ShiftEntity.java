package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long id;

    @Column(name = "shift_day", nullable = false, length = 20)
    private LocalDate day;

    @Column(name = "entry_time", nullable = false, length = 20)
    private LocalDateTime entryTime;

    @Column(name = "exit_time", nullable = false, length = 20)
    private LocalDateTime exitTime;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;
}
