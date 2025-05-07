package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "stocks")
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stocks_id_gen")
    @SequenceGenerator(name = "stocks_id_gen", sequenceName = "stock_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "stock_quantity", nullable = false, precision = 10, scale = 4)
    private BigDecimal stockQuantity;

}