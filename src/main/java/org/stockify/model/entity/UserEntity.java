package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "client_first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "client_last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "client_dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "client_email", nullable = false, length = 50)
    private String email;

    @Column(name = "client_phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "client_date_of_registration")
    @CreationTimestamp
    private LocalDate dateOfRegistration;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<SaleEntity> sales;


    @Column(name = "client_img")
    private String img;

}