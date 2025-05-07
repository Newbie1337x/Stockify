package org.stockify.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.stockify.Model.Enums.EmployeeStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 100, unique = true)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String lastName;
    @Column(nullable = false)
    private EmployeeStatus status;
    @Column(nullable = false)
    private Boolean active;
}
