package org.stockify.controller.transaction;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.service.PurchaseService;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeID}/pos/{posID}/transactions/purchase")
public class TransactionPurchaseController {

    private final PurchaseService purchaseService;

    public TransactionPurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> create(
            @Valid @RequestBody PurchaseRequest request,
            @PathVariable Long storeID,
            @PathVariable Long posID) {
        PurchaseResponse response = purchaseService.createPurchase(request, storeID, posID);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
