package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "total")
    private Long total;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "session_pos_id")
    private SessionPosEntity sessionPosEntity;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;



}
