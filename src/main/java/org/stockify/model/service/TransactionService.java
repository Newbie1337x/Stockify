package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.model.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Tran saveTransaction() {
    }


}
