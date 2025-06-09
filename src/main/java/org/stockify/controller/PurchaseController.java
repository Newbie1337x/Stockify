package org.stockify.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.service.PurchaseService;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping(("/{storeID}/{posID}" ) )
    public ResponseEntity<PurchaseResponse> create(@Valid @RequestBody PurchaseRequest request, @PathVariable Long storeID, @PathVariable Long posID) {
        return new ResponseEntity<>(purchaseService.createPurchase(request,storeID,posID), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseResponse>> list(Pageable pageable, PurchaseFilterRequest filter) {
        return ResponseEntity.ok(purchaseService.getAllPurchases(pageable, filter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
