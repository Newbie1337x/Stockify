package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.DetailTransactionRequest;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.DetailTransactionResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.DetailTransactionEntity;
import org.stockify.model.entity.TransactionEntity;
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
    private final DetailTransactionMapper detailTransactionMapper;
    private final StoreRepository storeRepository;
    private final PosRepository posRepository;
    private final SessionPosService sessionPosService;
    private final ProductRepository productRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper, DetailTransactionMapper detailTransactionMapper, StoreRepository storeRepository, PosRepository posRepository, SessionPosService sessionPosService, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.detailTransactionMapper = detailTransactionMapper;
        this.storeRepository = storeRepository;
        this.posRepository = posRepository;
        this.sessionPosService = sessionPosService;
        this.productRepository = productRepository;
    }

    public TransactionResponse saveTransaction(TransactionRequest request) {
        return transactionMapper
                .toDto(transactionRepository
                                .save(transactionMapper
                                        .toEntity(request)));
    }

    public TransactionResponse createTransaction(TransactionRequest request,Long idLocal,Long idPos)
    {
        Set<DetailTransactionEntity> detailTransactions = request
                .getDetailTransactions()
                .stream()
                .map(detailTransactionMapper::toEntity)
                .collect(Collectors.toSet());

        detailTransactions
                .forEach
                        (detailTransaction
                                -> detailTransaction.setSubtotal(
                                        productRepository
                                                .findById(detailTransaction
                                                        .getProduct()
                                                        .getId())
                                                .orElseThrow(
                                                        () -> new NotFoundException("Product with ID " + detailTransaction.getProduct().getId() + " not found."))
                                                .getPrice().multiply(detailTransaction.getQuantity()))
                        );

        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setDetailTransactions(detailTransactions);
        transactionEntity.setSessionPosEntity
                (sessionPosService.findByIdPosAndCloseTime(idPos,null));

        transactionEntity.setStore(storeRepository.findById(idLocal).orElseThrow());

        transactionEntity.setTotal(
                detailTransactions
                        .stream()
                        .map(DetailTransactionEntity::getSubtotal)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO)
        );

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
