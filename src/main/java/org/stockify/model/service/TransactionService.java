package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.model.repository.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProviderRepository providerRepository;
    private final SessionPosRepository sessionPosRepository;
    private final StoreRepository storeRepository;
    /*
    private final ClientRepository clientRepository;
    private final DetailTransactionRepository detailTransactionRepository;
     */

    public TransactionService(TransactionRepository transactionRepository,
                              ProviderRepository providerRepository,
                              SessionPosRepository sessionPosRepository,
                              StoreRepository storeRepository
                              //,ClientRepository clientRepository,
                              //DetailTransactionRepository detailTransactionRepository
                              ) {
        this.transactionRepository = transactionRepository;
        this.providerRepository = providerRepository;
        this.sessionPosRepository = sessionPosRepository;
        this.storeRepository = storeRepository;
        //this.clientRepository = clientRepository;
        //this.detailTransactionRepository = detailTransactionRepository;
    }


}
