package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.TransactionRepository;
@Transactional
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }
    @Transactional
    public TransactionResponse saveTransaction(TransactionRequest request) {
        return transactionMapper.toDto(transactionRepository.save(transactionMapper.toEntity(request)));
    }


}
