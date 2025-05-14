package org.stockify.model.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "providers")


//TEMPORAL , MAS ADELANTE IMPLEMENTAR FILTER PARA QUE POR EJEMPLO EL ADMINISTRADOR PUEDA VER LOS DESACTIVADOS TAMBIEN.
@SQLRestriction("active = true")


public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id", nullable = false)
    private Long id;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(nullable = false, length = 100)
    private String taxId;

    @Column(name = "tax_address", nullable = false)
    private String taxAddress;

    @Column
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "contact_name", nullable = false)
    private String name;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean active = true;
}