package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.stockify.model.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String lastName;
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private Boolean active;
}
