package org.stockify.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
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

/**
 * Service responsible for retrieving audit revisions of Purchase, Sale,
 * and Transaction entities using Hibernate Envers.
 * <p>
 * It fetches all revisions of these entities, loads related transaction details
 * where applicable, and maps the results into audit DTOs for external use.
 */
@Service
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TransactionMapper transactionMapper;

    /**
     * Constructs the AuditService with the required TransactionMapper dependency.
     *
     * @param transactionMapper Mapper to convert TransactionEntity to DTO.
     */
    public AuditService(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    /**
     * Retrieves all audit revisions for Purchase entities.
     * <p>
     * For each Purchase entity, this method fetches all its audit revisions,
     * loads associated transaction details manually if present,
     * and converts the data into a list of PurchaseAuditDTOs.
     *
     * @return List of PurchaseAuditDTO containing revision data and transaction details.
     */
    public List<PurchaseAuditDTO> getAllPurchaseAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<PurchaseEntity> purchases = entityManager.createQuery("SELECT p FROM PurchaseEntity p", PurchaseEntity.class).getResultList();
        List<PurchaseAuditDTO> auditList = new ArrayList<>();

        for (PurchaseEntity purchase : purchases) {
            List<Number> revisions = auditReader.getRevisions(PurchaseEntity.class, purchase.getId());
            for (Number rev : revisions) {
                PurchaseEntity auditedPurchase = auditReader.find(PurchaseEntity.class, purchase.getId(), rev);

                // Determine a revision type: "ADD" for the first revision, else "MOD"
                String revisionTypeStr = rev.equals(revisions.getFirst()) ? "ADD" : "MOD";

                TransactionEntity transaction = auditedPurchase.getTransaction();

                // Load transaction details manually if transaction exists
                if (transaction != null) {
                    List<DetailTransactionEntity> details = entityManager.createQuery(
                                    "SELECT d FROM DetailTransactionEntity d WHERE d.transaction.id = :txId",
                                    DetailTransactionEntity.class)
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
     * Retrieves all audit revisions for Sale entities.
     * <p>
     * For each Sale entity, this method fetches all its audit revisions,
     * loads associated transaction details manually if present,
     * and converts the data into a list of SaleAuditDTOs including client information.
     *
     * @return List of SaleAuditDTO containing revision data, transaction details, and client ID.
     */
    public List<SaleAuditDTO> getAllSaleAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<SaleEntity> sales = entityManager.createQuery("SELECT s FROM SaleEntity s", SaleEntity.class).getResultList();
        List<SaleAuditDTO> auditList = new ArrayList<>();

        for (SaleEntity sale : sales) {
            List<Number> revisions = auditReader.getRevisions(SaleEntity.class, sale.getId());
            for (Number rev : revisions) {
                SaleEntity auditedSale = auditReader.find(SaleEntity.class, sale.getId(), rev);

                // Determine a revision type: "ADD" for the first revision, else "MOD"
                String revisionTypeStr = rev.equals(revisions.getFirst()) ? "ADD" : "MOD";

                TransactionEntity transaction = auditedSale.getTransaction();

                // Load transaction details manually if transaction exists
                if (transaction != null) {
                    List<DetailTransactionEntity> details = entityManager.createQuery(
                                    "SELECT d FROM DetailTransactionEntity d WHERE d.transaction.id = :txId",
                                    DetailTransactionEntity.class)
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
     * Retrieves all audit revisions for Transaction entities.
     * <p>
     * For each Transaction entity, this method fetches all its audit revisions
     * and converts them into a list of TransactionAuditDTOs containing
     * revision metadata and key transaction properties.
     *
     * @return List of TransactionAuditDTO containing transaction audit information.
     */
    public List<TransactionAuditDTO> getAllTransactionAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<TransactionEntity> transactions = entityManager.createQuery("SELECT t FROM TransactionEntity t", TransactionEntity.class).getResultList();
        List<TransactionAuditDTO> auditList = new ArrayList<>();

        for (TransactionEntity transaction : transactions) {
            List<Number> revisions = auditReader.getRevisions(TransactionEntity.class, transaction.getId());
            for (Number rev : revisions) {
                TransactionEntity auditedTransaction = auditReader.find(TransactionEntity.class, transaction.getId(), rev);

                // Determine a revision type: "ADD" for the first revision, else "MOD"
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
