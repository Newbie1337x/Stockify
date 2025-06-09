package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.DetailTransactionRequest;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.DetailTransactionResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.DetailTransactionEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.DetailTransactionMapper;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.PosRepository;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final StoreRepository storeRepository;
    private final SessionPosService sessionPosService;
    private final ProductRepository productRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper, DetailTransactionMapper detailTransactionMapper, StoreRepository storeRepository, PosRepository posRepository, SessionPosService sessionPosService, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.storeRepository = storeRepository;
        this.sessionPosService = sessionPosService;
        this.productRepository = productRepository;
    }

    public TransactionResponse saveTransaction(TransactionRequest request) {
        return transactionMapper
                .toDto(transactionRepository
                                .save(transactionMapper
                                        .toEntity(request)));
    }

    public TransactionResponse createTransaction(TransactionRequest request, Long idLocal, Long idPos, TransactionType type)
    {
//        Set<DetailTransactionEntity> detailTransactions = request
//                .getDetailTransactions()
//                .stream()
//                .map(detailTransactionMapper::toEntity)
//                .collect(Collectors.toSet());


        //Hice esto porque si no daba null pointer porque estaba intentando hacer un toEntity con productos sin mapear

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
        transactionEntity.setSessionPosEntity
                (sessionPosService.findByIdPosAndCloseTime(idPos,null));

        StoreEntity store = storeRepository.findById(idLocal).orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);


        transactionEntity.setTotal(
                detailTransactions
                        .stream()
                        .map(DetailTransactionEntity::getSubtotal)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO)
        );
        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);


        return transactionMapper.toDto(transactionRepository.save(transactionEntity));
    }

    public List<TransactionResponse> findAll()
    {
        return transactionRepository
                .findAll()
                .stream()
                .map(transactionMapper::toDto).toList();
    }




}
