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
@ToString
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

    @OneToMany(mappedBy = "shiftEntity")

    private List<SessionPosEntity> posSessionEntities;
    @ManyToMany
    @JoinTable(
            name = "shift_employee", // nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "shift_id"), // columna que referencia a ShiftEntity
            inverseJoinColumns = @JoinColumn(name = "employee_id") // columna que referencia a EmployeeEntity
    )
    private List<EmployeeEntity> employeeEntities;
}