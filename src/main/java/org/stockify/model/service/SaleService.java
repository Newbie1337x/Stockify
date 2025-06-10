package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.SaleEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SaleMapper;
import org.stockify.model.repository.ClientRepository;
import org.stockify.model.repository.SaleRepository;
import org.stockify.model.repository.TransactionRepository;

@Service
public class SaleService {

    private final StockService stockService;
    private final TransactionService transactionService;
    private final SaleMapper saleMapper;
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final SaleRepository saleRepository;

    public SaleService(StockService stockService, TransactionService transactionService, SaleMapper saleMapper, TransactionRepository transactionRepository, ClientRepository clientRepository, SaleRepository saleRepository) {
        this.stockService = stockService;
        this.transactionService = transactionService;
        this.saleMapper = saleMapper;
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.saleRepository = saleRepository;
    }


    @Transactional
    public SaleResponse createSale(SaleRequest request, long storeID, long posID){
        request.getTransaction().getDetailTransactions()
                .forEach(detail -> stockService.decreaseStock(detail.getProductID(), storeID, detail.getQuantity()));

        TransactionResponse transaction = transactionService.createTransaction(request.getTransaction(), storeID, posID, TransactionType.SALE);
        SaleEntity sale = saleMapper.toEntity(request);
        if (request.getClientId() != null) {
            sale.setClient(clientRepository.findById(request.getClientId()).orElseThrow(() -> new NotFoundException("Client not found with ID"+ request.getClientId())));
        }
        sale.setTransaction(transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new NotFoundException("Transaction not found")));


        return saleMapper.toResponseDTO(saleRepository.save(sale));
    }
}
