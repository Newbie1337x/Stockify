package org.stockify.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.PurchaseAuditDTO;
import org.stockify.model.dto.SaleAuditDTO;
import org.stockify.model.dto.TransactionAuditDTO;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.entity.SaleEntity;
import org.stockify.model.entity.TransactionEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    // Obtener todas las auditorías de compras
    public List<PurchaseAuditDTO> getAllPurchaseAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<PurchaseEntity> purchases = entityManager.createQuery("SELECT p FROM PurchaseEntity p", PurchaseEntity.class).getResultList();
        List<PurchaseAuditDTO> auditList = new ArrayList<>();

        for (PurchaseEntity purchase : purchases) {
            List<Number> revisions = auditReader.getRevisions(PurchaseEntity.class, purchase.getId());
            for (Number rev : revisions) {
                PurchaseEntity auditedPurchase = auditReader.find(PurchaseEntity.class, purchase.getId(), rev);
                String revisionTypeStr = rev.equals(revisions.get(0)) ? "ADD" : "MOD";
                PurchaseAuditDTO auditDTO = PurchaseAuditDTO.builder()
                        .revision(rev.longValue())
                        .revisionType(revisionTypeStr)
                        .id(auditedPurchase.getId())
                        .transactionId(auditedPurchase.getTransaction() != null ? auditedPurchase.getTransaction().getId() : null)
                        .providerId(auditedPurchase.getProvider() != null ? auditedPurchase.getProvider().getId() : null)
                        .build();
                auditList.add(auditDTO);
            }
        }
        return auditList;
    }

    // Obtener todas las auditorías de ventas
    public List<SaleAuditDTO> getAllSaleAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<SaleEntity> sales = entityManager.createQuery("SELECT s FROM SaleEntity s", SaleEntity.class).getResultList();
        List<SaleAuditDTO> auditList = new ArrayList<>();

        for (SaleEntity sale : sales) {
            List<Number> revisions = auditReader.getRevisions(SaleEntity.class, sale.getId());
            for (Number rev : revisions) {
                SaleEntity auditedSale = auditReader.find(SaleEntity.class, sale.getId(), rev);
                String revisionTypeStr = rev.equals(revisions.get(0)) ? "ADD" : "MOD";
                SaleAuditDTO auditDTO = SaleAuditDTO.builder()
                        .revision(rev.longValue())
                        .revisionType(revisionTypeStr)
                        .id(auditedSale.getId())
                        .transactionId(auditedSale.getTransaction() != null ? auditedSale.getTransaction().getId() : null)
                        .clientId(auditedSale.getClient() != null ? auditedSale.getClient().getId() : null)
                        .build();
                auditList.add(auditDTO);
            }
        }
        return auditList;
    }

    // Obtener todas las auditorías de transacciones
    public List<TransactionAuditDTO> getAllTransactionAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<TransactionEntity> transactions = entityManager.createQuery("SELECT t FROM TransactionEntity t", TransactionEntity.class).getResultList();
        List<TransactionAuditDTO> auditList = new ArrayList<>();

        for (TransactionEntity transaction : transactions) {
            List<Number> revisions = auditReader.getRevisions(TransactionEntity.class, transaction.getId());
            for (Number rev : revisions) {
                TransactionEntity auditedTransaction = auditReader.find(TransactionEntity.class, transaction.getId(), rev);
                String revisionTypeStr = rev.equals(revisions.get(0)) ? "ADD" : "MOD";
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
