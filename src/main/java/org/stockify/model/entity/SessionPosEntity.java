    package org.stockify.model.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.ColumnDefault;

    import java.math.BigDecimal;
    import java.sql.Date;
    import java.sql.Timestamp;
    import java.time.LocalDateTime;
    import java.util.Set;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    @Entity
    @Table(name = "session_pos")
    public class SessionPosEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "opening_time")
        private LocalDateTime openingTime;

        @Column(name = "close_time")
        private LocalDateTime closeTime;

        @Column(name = "opening_amount", nullable = false)
        private BigDecimal openingAmount;

        @Column(name = "close_amount")
        private BigDecimal closeAmount;

        @Column(name = "cash_difference")
        private BigDecimal cashDifference;

        @Column(name = "expected_amount")
        private BigDecimal expectedAmount;

        @ManyToOne
        @JoinColumn(name = "employee_id")
        private EmployeeEntity employee;

        @ManyToOne()
        @JoinColumn(name = "pos_id")
        private PosEntity posEntity;
        @OneToMany(mappedBy = "sessionPosEntity", fetch = FetchType.LAZY)
        private Set<TransactionEntity> transactions;
    }