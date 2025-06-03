package org.stockify.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.stockify.model.enums.PayMethod;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
//@EntityListeners(AuditingEntityListener.class)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column
    private Double total;

    @NotNull
    @Column(nullable = false)
    // @CreatedDate  Solamente si se coloca @EntityListeners en la entidad y @EnableJpaAuditing en la clase @Configuration (para persistir automaticamente la hora)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_session_pos")
    private SessionPosEntity sessionPos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_provider")
    private ProviderEntity provider;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_employee")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client")
    private ClientEntity client;

    /*
    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
    private Set<DetailTransactionEntity> detailTransaction;
    */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_store")
    private StoreEntity store;

    @Column
    private String description;

    @Column(name = "pay_method",nullable = false)
    private PayMethod payMethod;

    @Column
    private String transactionType;

    @PrePersist                 //Metodo que se aplica automaticamente antes que la entidad se persista en la BDD
    public void prePersist(){
        this.time = LocalDateTime.now();
    }
}
