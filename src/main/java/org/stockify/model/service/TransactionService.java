package org.stockify.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.transaction.TransactionCreatedRequest;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionCreatedResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.DetailTransactionEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.repository.PosRepository;
import org.stockify.model.repository.SessionPosRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service responsible for handling transaction-related operations.
 * Supports creating monetary transactions (e.g., cash movements) and detailed transactions
 * (e.g., purchases and sales that involve products).
 * All operations are transactional to ensure data consistency.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PosRepository posRepository;
    private final SessionPosRepository sessionPosRepository;

    /**
     * Creates and saves a monetary transaction without product details.
     * Typically used for financial operations such as cash withdrawals,
     * payments to third-party services (e.g., taxis), or other money movements.
     *
     * @param request the basic transaction data including amount and description
     * @param idLocal the ID of the store where the transaction takes place
     * @param idPos   the ID of the POS (Point of Sale) terminal involved
     * @param type    the type of transaction (e.g., CASH_OUT, TRANSFER)
     * @return a response containing details of the created transaction
     * @throws NotFoundException if the POS or store is not found
     */
    public TransactionCreatedResponse saveTransaction(TransactionCreatedRequest request, Long idLocal, Long idPos, TransactionType type) {
        if (!posRepository.existsById(idPos)) {
            throw new NotFoundException("POS with ID " + idPos + " not found.");
        }

        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setTotal(request.getTotalAmount());

        transactionEntity.setSessionPosEntity(
                sessionPosRepository.findByPosEntity_IdAndCloseTime(idPos, null)
                        .orElseThrow(() -> new NotFoundException("POS with ID " + idPos + " not found."))
        );

        StoreEntity store = storeRepository.findById(idLocal)
                .orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);
        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);

        return transactionMapper.toDtoCreated(transactionRepository.save(transactionEntity));
    }

    /**
     * Creates and saves a transaction that includes product details.
     * Used in sale and purchase operations, this method builds a transaction entity
     * with all the associated product quantities, prices, and subtotals.
     *
     * @param request the detailed transaction data including a product list and quantities
     * @param idLocal the ID of the store where the transaction occurs
     * @param idPos   the ID of the POS terminal involved
     * @param type    the type of transaction (SALE or PURCHASE)
     * @return the saved TransactionEntity with all details persisted
     * @throws NotFoundException if any required entity (product, store, or POS) is not found
     */
    public TransactionEntity createTransaction(TransactionRequest request, Long idLocal, Long idPos, TransactionType type) {

        if (!posRepository.existsById(idPos)) {
            throw new NotFoundException("POS with ID " + idPos + " not found.");
        }

        // Convert each detail to an entity, linking the product and calculating subtotal
        Set<DetailTransactionEntity> detailTransactions = request
                .getDetailTransactions()
                .stream()
                .map(detailRequest -> {
                    ProductEntity product = productRepository.findById(detailRequest.getProductID())
                            .orElseThrow(() -> new NotFoundException("Product with ID " + detailRequest.getProductID() + " not found."));

                    DetailTransactionEntity entity = new DetailTransactionEntity();
                    entity.setProduct(product);

                    BigDecimal quantity = BigDecimal.valueOf(detailRequest.getQuantity());
                    entity.setQuantity(quantity);

                    entity.setSubtotal(product.getPrice().multiply(quantity));

                    return entity;
                })
                .collect(Collectors.toSet());

        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setDetailTransactions(detailTransactions);

        // Link each detail to the parent transaction
        detailTransactions.forEach(detail -> detail.setTransaction(transactionEntity));

        transactionEntity.setSessionPosEntity(
                sessionPosRepository.findByPosEntity_IdAndCloseTime(idPos, null)
                        .orElseThrow(() -> new NotFoundException("POS with ID " + idPos + " not found."))
        );

        StoreEntity store = storeRepository.findById(idLocal)
                .orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);

        // Calculate the total amount from all subtotals
        transactionEntity.setTotal(
                detailTransactions.stream()
                        .map(DetailTransactionEntity::getSubtotal)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO)
        );

        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);

        return transactionRepository.save(transactionEntity);
    }

    /**
     * Retrieves all transactions stored in the system.
     *
     * @return a list of TransactionResponse objects representing all registered transactions
     */
    public List<TransactionResponse> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }
}
