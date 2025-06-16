package org.stockify.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.audit.PurchaseAuditDTO;
import org.stockify.dto.request.audit.SaleAuditDTO;
import org.stockify.dto.request.audit.TransactionAuditDTO;
import org.stockify.dto.request.audit.filter.PurchaseAuditFilterRequest;
import org.stockify.dto.request.audit.filter.SaleAuditFilterRequest;
import org.stockify.dto.request.audit.filter.TransactionAuditFilterRequest;
import org.stockify.model.entity.DetailTransactionEntity;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.entity.SaleEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.specification.PurchaseAuditSpecification;
import org.stockify.model.specification.SaleAuditSpecification;
import org.stockify.model.specification.TransactionAuditSpecification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionMapper transactionMapper;


    /**
     * Obtiene todas las auditorías de compras con paginación y filtrado.
     *
     * @param pageable Información de paginación
     * @param filter Filtros a aplicar
     * @return Página de auditorías de compras que cumplen con los filtros
     */
    public Page<PurchaseAuditDTO> getAllPurchaseAudits(Pageable pageable, PurchaseAuditFilterRequest filter) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<PurchaseEntity> purchases = entityManager.createQuery("SELECT p FROM PurchaseEntity p", PurchaseEntity.class).getResultList();
        List<PurchaseAuditDTO> auditList = new ArrayList<>();

        for (PurchaseEntity purchase : purchases) {
            List<Number> revisions = auditReader.getRevisions(PurchaseEntity.class, purchase.getId());
            for (Number rev : revisions) {
                PurchaseEntity auditedPurchase = auditReader.find(PurchaseEntity.class, purchase.getId(), rev);
                String revisionTypeStr = rev.equals(revisions.getFirst()) ? "ADD" : "MOD";

                TransactionEntity transaction = auditedPurchase.getTransaction();

                // 🟡 Cargar detalles de la transacción manualmente si existe
                if (transaction != null) {
                    List<DetailTransactionEntity> details = entityManager.createQuery("""
                    SELECT d FROM DetailTransactionEntity d
                    WHERE d.transaction.id = :txId
                """, DetailTransactionEntity.class)
                            .setParameter("txId", transaction.getId())
                            .getResultList();

                    transaction.setDetailTransactions(new HashSet<>(details));
                }

                PurchaseAuditDTO auditDTO = PurchaseAuditDTO.builder()
                        .revision(rev.longValue())
                        .revisionType(revisionTypeStr)
                        .id(auditedPurchase.getId())
                        .transaction(transactionMapper.toDto(transaction))
                        .build();

                auditList.add(auditDTO);
            }
        }

        // Apply filters
        List<PurchaseAuditDTO> filteredList = auditList.stream()
            .filter(audit -> filter.getRevision() == null || audit.getRevision().equals(filter.getRevision()))
            .filter(audit -> filter.getRevisionType() == null || audit.getRevisionType().equals(filter.getRevisionType()))
            .filter(audit -> filter.getPurchaseId() == null || audit.getId().equals(filter.getPurchaseId()))
            .filter(audit -> filter.getTransactionId() == null || 
                    (audit.getTransaction() != null && audit.getTransaction().getId().equals(filter.getTransactionId())))
            .collect(Collectors.toList());

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredList.size());

        if (start > filteredList.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredList.size());
        }

        return new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());
    }

    /**
     * Obtiene todas las auditorías de compras.
     *
     * @return Lista de auditorías de compras.
     * @deprecated Use {@link #getAllPurchaseAudits(Pageable, PurchaseAuditFilterRequest)} instead
     */
    @Deprecated
    public List<PurchaseAuditDTO> getAllPurchaseAudits() {
        return getAllPurchaseAudits(Pageable.unpaged(), new PurchaseAuditFilterRequest()).getContent();
    }


    /**
     * Obtiene todas las auditorías de ventas con paginación y filtrado.
     *
     * @param pageable Información de paginación
     * @param filter Filtros a aplicar
     * @return Página de auditorías de ventas que cumplen con los filtros
     */
    public Page<SaleAuditDTO> getAllSaleAudits(Pageable pageable, SaleAuditFilterRequest filter) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<SaleEntity> sales = entityManager.createQuery("SELECT s FROM SaleEntity s", SaleEntity.class).getResultList();
        List<SaleAuditDTO> auditList = new ArrayList<>();

        for (SaleEntity sale : sales) {
            List<Number> revisions = auditReader.getRevisions(SaleEntity.class, sale.getId());
            for (Number rev : revisions) {
                SaleEntity auditedSale = auditReader.find(SaleEntity.class, sale.getId(), rev);
                String revisionTypeStr = rev.equals(revisions.getFirst()) ? "ADD" : "MOD";

                TransactionEntity transaction = auditedSale.getTransaction();

                if (transaction != null) {
                    List<DetailTransactionEntity> details = entityManager.createQuery("""
                    SELECT d FROM DetailTransactionEntity d
                    WHERE d.transaction.id = :txId
                """, DetailTransactionEntity.class)
                            .setParameter("txId", transaction.getId())
                            .getResultList();

                    transaction.setDetailTransactions(new HashSet<>(details));
                }

                SaleAuditDTO auditDTO = SaleAuditDTO.builder()
                        .revision(rev.longValue())
                        .revisionType(revisionTypeStr)
                        .id(auditedSale.getId())
                        .transaction(transactionMapper.toDto(transaction))
                        .clientId(auditedSale.getClient() != null ? auditedSale.getClient().getId() : null)
                        .build();

                auditList.add(auditDTO);
            }
        }

        // Apply filters
        List<SaleAuditDTO> filteredList = auditList.stream()
            .filter(audit -> filter.getRevision() == null || audit.getRevision().equals(filter.getRevision()))
            .filter(audit -> filter.getRevisionType() == null || audit.getRevisionType().equals(filter.getRevisionType()))
            .filter(audit -> filter.getSaleId() == null || audit.getId().equals(filter.getSaleId()))
            .filter(audit -> filter.getTransactionId() == null || 
                    (audit.getTransaction() != null && audit.getTransaction().getId().equals(filter.getTransactionId())))
            .filter(audit -> filter.getClientId() == null || 
                    (audit.getClientId() != null && audit.getClientId().equals(filter.getClientId())))
            .collect(Collectors.toList());

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredList.size());

        if (start > filteredList.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredList.size());
        }

        return new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());
    }

    /**
     * Obtiene todas las auditorías de ventas.
     *
     * @return Lista de auditorías de ventas.
     * @deprecated Use {@link #getAllSaleAudits(Pageable, SaleAuditFilterRequest)} instead
     */
    @Deprecated
    public List<SaleAuditDTO> getAllSaleAudits() {
        return getAllSaleAudits(Pageable.unpaged(), new SaleAuditFilterRequest()).getContent();
    }


    /**
     * Obtiene todas las auditorías de transacciones con paginación y filtrado.
     *
     * @param pageable Información de paginación
     * @param filter Filtros a aplicar
     * @return Página de auditorías de transacciones que cumplen con los filtros
     */
    public Page<TransactionAuditDTO> getAllTransactionAudits(Pageable pageable, TransactionAuditFilterRequest filter) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<TransactionEntity> transactions = entityManager.createQuery("SELECT t FROM TransactionEntity t", TransactionEntity.class).getResultList();
        List<TransactionAuditDTO> auditList = new ArrayList<>();

        for (TransactionEntity transaction : transactions) {
            List<Number> revisions = auditReader.getRevisions(TransactionEntity.class, transaction.getId());
            for (Number rev : revisions) {
                TransactionEntity auditedTransaction = auditReader.find(TransactionEntity.class, transaction.getId(), rev);
                String revisionTypeStr = rev.equals(revisions.getFirst()) ? "ADD" : "MOD";

                TransactionAuditDTO auditDTO = TransactionAuditDTO.builder()
                        .revision(rev.longValue())
                        .revisionType(revisionTypeStr)
                        .id(auditedTransaction.getId())
                        .total(auditedTransaction.getTotal())
                        .dateTime(auditedTransaction.getDateTime())
                        .paymentMethod(auditedTransaction.getPaymentMethod())
                        .description(auditedTransaction.getDescription())
                        .type(auditedTransaction.getType())
                        .storeId(auditedTransaction.getStore() != null ? auditedTransaction.getStore().getId() : null)
                        .sessionPosId(auditedTransaction.getSessionPosEntity() != null ? auditedTransaction.getSessionPosEntity().getId() : null)
                        .build();

                auditList.add(auditDTO);
            }
        }

        // Apply filters
        List<TransactionAuditDTO> filteredList = auditList.stream()
            .filter(audit -> filter.getRevision() == null || audit.getRevision().equals(filter.getRevision()))
            .filter(audit -> filter.getRevisionType() == null || audit.getRevisionType().equals(filter.getRevisionType()))
            .filter(audit -> filter.getTransactionId() == null || audit.getId().equals(filter.getTransactionId()))
            .filter(audit -> filter.getPaymentMethod() == null || audit.getPaymentMethod() == filter.getPaymentMethod())
            .filter(audit -> filter.getType() == null || audit.getType() == filter.getType())
            .filter(audit -> filter.getStoreId() == null || (audit.getStoreId() != null && audit.getStoreId().equals(filter.getStoreId())))
            .filter(audit -> filter.getSessionPosId() == null || (audit.getSessionPosId() != null && audit.getSessionPosId().equals(filter.getSessionPosId())))
            .filter(audit -> filter.getFromDate() == null || (audit.getDateTime() != null && !audit.getDateTime().isBefore(filter.getFromDate())))
            .filter(audit -> filter.getToDate() == null || (audit.getDateTime() != null && !audit.getDateTime().isAfter(filter.getToDate())))
            .collect(Collectors.toList());

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredList.size());

        if (start > filteredList.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredList.size());
        }

        return new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());
    }

    /**
     * Obtiene todas las auditorías de transacciones.
     *
     * @return Lista de auditorías de transacciones.
     * @deprecated Use {@link #getAllTransactionAudits(Pageable, TransactionAuditFilterRequest)} instead
     */
    @Deprecated
    public List<TransactionAuditDTO> getAllTransactionAudits() {
        return getAllTransactionAudits(Pageable.unpaged(), new TransactionAuditFilterRequest()).getContent();
    }
}
