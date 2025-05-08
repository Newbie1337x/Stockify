package org.stockify.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_gen")
    @SequenceGenerator(name = "products_id_gen", sequenceName = "products_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("0")
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories;

}