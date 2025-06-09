package org.stockify.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.mapper.PurchaseMapper;
import org.stockify.model.repository.PurchaseRepository;
import org.stockify.model.specification.PurchaseSpecification;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    //-- CRUD operations --

    public PurchaseResponse createPurchase(PurchaseRequest request) {
        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchaseMapper.toEntity(request)));
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
