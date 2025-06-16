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
import org.stockify.model.repository.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * This is typically used for simple financial operations such as cash withdrawals,
     * paying external services (e.g., taxis), or recording generic money movements.
     *
     * @param request the basic transaction data
     * @param idLocal the ID of the store where the transaction occurs
     * @param idPos the ID of the POS terminal used
     * @param type the type of transaction (e.g., CASH_OUT, TRANSFER)
     * @return a response summarizing the created transaction
     */
    public TransactionCreatedResponse saveTransaction(TransactionCreatedRequest request, Long idLocal, Long idPos, TransactionType type) {
        if (!posRepository.existsById(idPos)) {
            throw new NotFoundException("POS with ID " + idPos + " not found.");
        }
        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setTotal(request.getTotalAmount());

        // Setear la sesión del POS
        transactionEntity.setSessionPosEntity(
                sessionPosRepository.findByPosEntity_IdAndCloseTime
                        (idPos, null).orElseThrow
                        (() -> new NotFoundException("POS with ID " + idPos + " not found."))
        );
        // Buscar y setear el local
        StoreEntity store = storeRepository.findById(idLocal)
                .orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);
        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);

        return transactionMapper.toDtoCreated(transactionRepository
                .save(transactionEntity));
    }

    /**
     * Creates and persists a complete transaction with product details.
     * This is used specifically by sales and purchase services to register operations
     * that involve specific products, their quantities, and prices.
     *
     * @param request the detailed transaction request including products and quantities
     * @param idLocal the ID of the store associated with the transaction
     * @param idPos the ID of the POS terminal involved
     * @param type the type of transaction (SALE or PURCHASE)
     * @return the persisted TransactionEntity
     */
    public TransactionEntity createTransaction(TransactionRequest request, Long idLocal, Long idPos, TransactionType type) {

        if (!posRepository.existsById(idPos)) {
            throw new NotFoundException("POS with ID " + idPos + " not found.");
        }

        // Convertir cada detalle a entidad, seteando producto, cantidad, subtotal
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

        // Crear la transacción base
        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setDetailTransactions(detailTransactions);


        detailTransactions.forEach
                (detail -> detail.setTransaction(transactionEntity));

        // Setear la sesión del POS
        transactionEntity.setSessionPosEntity(
                sessionPosRepository.findByPosEntity_IdAndCloseTime
                        (idPos, null).orElseThrow
                        (() -> new NotFoundException("POS with ID " + idPos + " not found."))
        );

        // Buscar y setear el local
        StoreEntity store = storeRepository.findById(idLocal)
                .orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);

        // Calcular el total
        transactionEntity.setTotal(
                detailTransactions
                        .stream()
                        .map(DetailTransactionEntity::getSubtotal)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO)
        );

        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);

        // Guardar y devolver el response
        return transactionRepository.save(transactionEntity);
    }


    /**
     * Retrieves all transactions from the repository.
     *
     * @return a list of TransactionResponse objects representing all transactions
     */
    public List<TransactionResponse> findAll()
    {
        return transactionRepository
                .findAll()
                .stream()
                .map(transactionMapper::toDto).toList();
    }


}
