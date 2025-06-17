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
import org.stockify.model.entity.PosEntity;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PurchaseMapper;
import org.stockify.model.repository.PosRepository;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.repository.PurchaseRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.specification.PurchaseSpecification;

/**
 * Service class responsible for handling business logic related to purchases.
 * It provides operations for creating, updating, deleting, and querying purchases,
 * as well as managing related stock updates and transaction associations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final StockService stockService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final ProviderRepository providerRepository;
    private final PosRepository posRepository;

    /**
     * Creates a new purchase and updates the stock of the corresponding products.
     *
     * @param request the DTO containing the details of the purchase to be created
     * @param posID the ID of the point of sale associated with the purchase
     * @return the response DTO with the created purchase details
     * @throws NotFoundException if the transaction or provider cannot be found
     */
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request, Long posID) {

        PosEntity posEntity = posRepository.findById(posID)
                .orElseThrow
                        (() -> new NotFoundException("POS with ID " + posID + " not found."));
        Long localId = posEntity.getStore().getId();

        request.getTransaction().getDetailTransactions()
                .forEach(detail -> stockService.increaseStock(
                        detail.getProductID(), localId, detail.getQuantity()));


        TransactionEntity transaction = transactionService.createTransaction(
                request.getTransaction(), localId, posID, TransactionType.PURCHASE);

        PurchaseEntity purchase = purchaseMapper.toEntity(request);

        purchase.setTransaction(transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new NotFoundException("Transaction not found")));

        purchase.setProvider(providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new NotFoundException("Provider not found")));

        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchase));
    }

    /**
     * Updates an existing purchase.
     *
     * @param id the ID of the purchase to update
     * @param request the DTO containing the updated purchase data
     * @return the response DTO with the updated purchase details
     */
    public PurchaseResponse updatePurchase(Long id, PurchaseRequest request) {
        PurchaseEntity purchaseEntity = purchaseMapper.toEntity(request);
        purchaseEntity.setId(id);
        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchaseEntity));
    }

    /**
     * Deletes a purchase by its ID.
     *
     * @param id the ID of the purchase to delete
     */
    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    /**
     * Retrieves all purchases that match the given filter criteria and pagination configuration.
     *
     * @param pageable the pagination and sorting information
     * @param request the filter DTO containing optional transaction ID, provider ID, and purchase ID
     * @return a page of purchase response DTOs that match the criteria
     */
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable, PurchaseFilterRequest request) {
        Specification<PurchaseEntity> spec = Specification
                .where(PurchaseSpecification.ByTransactionId(request.getTransactionId()))
                .and(PurchaseSpecification.ByProviderId(request.getProviderId()))
                .and(PurchaseSpecification.ByPurchaseId(request.getPurchaseId()));
        return purchaseRepository.findAll(spec, pageable)
                .map(purchaseMapper::toResponseDTO);
    }

    /**
     * Retrieves a purchase by its ID.
     *
     * @param id the ID of the purchase to retrieve
     * @return the response DTO containing the found purchase details
     * @throws NotFoundException if the purchase with the given ID does not exist
     */
    public PurchaseResponse findById(Long id) {
        PurchaseEntity purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + id));
        return purchaseMapper.toResponseDTO(purchase);
    }
}
