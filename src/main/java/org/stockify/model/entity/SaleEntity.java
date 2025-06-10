package org.stockify.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Getter
@Setter
@Entity
@Table(name = "sales")
@Audited
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;



    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private TransactionEntity transaction;


    @ManyToOne
    @JoinColumn(name = "client_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ClientEntity client;

}