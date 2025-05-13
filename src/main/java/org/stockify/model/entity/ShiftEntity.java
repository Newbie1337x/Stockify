package org.stockify.model.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shifts")
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "entry_time")
    private LocalDateTime entryTime;
    @Column(name = "exit_time")
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
