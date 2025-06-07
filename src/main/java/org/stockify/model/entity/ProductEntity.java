package org.stockify.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "name", unique = true ,nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("0")
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    //Check
    @ColumnDefault("-1")
    @Column(name = "sku", unique = false)
    private String sku;

    @Column(name = "barcode" , unique = true)
    private String barcode;

    @Column(name = "brand")
    private String brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<StockEntity> stocks;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "products_providers",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    private Set<ProviderEntity> providers;

    public ProductEntity(){
        categories = new HashSet<>();
    }
}