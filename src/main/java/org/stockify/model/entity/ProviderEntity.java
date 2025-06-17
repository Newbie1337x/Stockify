package org.stockify.model.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import java.util.Set;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "providers")
@Where(clause = "active = true")

public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id", nullable = false ,unique = true)
    private Long id;

    @Column(name = "business_name", nullable = false ,unique = true)
    private String businessName;

    @Column(nullable = false, length = 100 , unique = true)
    private String taxId;

    @Column(name = "tax_address", nullable = false)
    private String taxAddress;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "contact_name", nullable = false)
    private String name;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "products_providers",
            joinColumns = @JoinColumn(name = "provider_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductEntity> productList;

    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    private Set<PurchaseEntity> purchaseList;
}
