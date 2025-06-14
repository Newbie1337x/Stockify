package org.stockify.controller.transaction;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.service.PurchaseService;

//Implement Hateoas and REST
@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeID}/pos/{posID}/transactions/purchases")
public class TransactionPurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @Transactional
    public ResponseEntity<PurchaseResponse> create(
            @Valid @RequestBody PurchaseRequest request,
            @PathVariable Long storeID,
            @PathVariable Long posID) {
        PurchaseResponse response = purchaseService.createPurchase(request, storeID, posID);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
