package org.stockify.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.audit.PurchaseAuditDTO;
import org.stockify.dto.request.audit.SaleAuditDTO;
import org.stockify.dto.request.audit.TransactionAuditDTO;
import org.stockify.model.entity.DetailTransactionEntity;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.entity.SaleEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.mapper.TransactionMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionMapper transactionMapper;


    /**
     * Obtiene todas las auditorías de compras.
     *
     * @return Lista de auditorías de compras.
     */
    public List<PurchaseAuditDTO> getAllPurchaseAudits() {
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
        return auditList;
    }


    /**
     * Obtiene todas las auditorías de ventas.
     *
     * @return Lista de auditorías de ventas.
     */
    public List<SaleAuditDTO> getAllSaleAudits() {
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
        return auditList;
    }


    /**
     * Obtiene todas las auditorías de transacciones.
     *
     * @return Lista de auditorías de transacciones.
     */
    public List<TransactionAuditDTO> getAllTransactionAudits() {
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
        return auditList;
    }
}
