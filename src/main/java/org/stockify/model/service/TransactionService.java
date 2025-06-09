package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.DetailTransactionRequest;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.DetailTransactionResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.TransactionRepository;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionResponse saveTransaction(TransactionRequest request) {
        return transactionMapper
                .toDto(transactionRepository
                                .save(transactionMapper
                                        .toEntity(request)));
    }

    public TransactionResponse createTransaction(TransactionRequest request, Set<DetailTransactionRequest> detailTransactionRequests)
    {
        return null;

    }

    public List<TransactionResponse> findAll()
    {
        return transactionRepository
                .findAll()
                .stream()
                .map(transactionMapper::toDto).toList();
    }




}
