package org.stockify.model.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PurchaseMapper;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.repository.PurchaseRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.specification.PurchaseSpecification;

@Service
@RequiredArgsConstructor

public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final StockService stockService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final ProviderRepository providerRepository;


    //-- CRUD operations --

    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request, Long storeID, Long posID) {

        request.getTransaction().getDetailTransactions()
                .forEach(detail -> stockService.increaseStock(detail.getProductID(), storeID, detail.getQuantity()));

        TransactionResponse transaction = transactionService.createTransaction(request.getTransaction(), storeID, posID, TransactionType.PURCHASE);
        PurchaseEntity purchase = purchaseMapper.toEntity(request);
        purchase.setTransaction(transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new NotFoundException("Transaction not found")));
        purchase.setProvider(providerRepository.findById(request.getProviderId()).orElseThrow(() -> new NotFoundException("Provider not found")));

        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchase));
    }

    public PurchaseResponse updatePurchase(Long id, PurchaseRequest request) {
        PurchaseEntity purchaseEntity = purchaseMapper.toEntity(request);
        purchaseEntity.setId(id);
        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchaseEntity));
    }

    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    public Page<PurchaseResponse> getAllPurchases(Pageable pageable, PurchaseFilterRequest request) {
        Specification<PurchaseEntity> spec = Specification
                .where(PurchaseSpecification.ByTransactionId(request.getTransactionId()))
                .and(PurchaseSpecification.ByProviderId(request.getProviderId()))
                .and(PurchaseSpecification.ByPurchaseId(request.getPurchaseId()));
        return purchaseRepository.findAll(spec, pageable)
                .map(purchaseMapper::toResponseDTO);
    }
    

}
