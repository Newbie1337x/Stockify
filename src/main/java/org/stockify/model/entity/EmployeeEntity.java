package org.stockify.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.stockify.model.enums.Status;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
@Where(clause = "active = true")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id",nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name" ,nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false,length = 8, unique = true)
    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String dni;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean active;

    @ManyToMany(mappedBy = "employeeEntities")
    private List<ShiftEntity> shiftEntities;


    @OneToMany(mappedBy = "employee")
    private List<SessionPosEntity> sessionPosEntities;

}
