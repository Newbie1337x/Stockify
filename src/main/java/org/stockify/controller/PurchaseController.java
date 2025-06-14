package org.stockify.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.assembler.PurchaseModelAssembler;
import org.stockify.model.service.PurchaseService;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PurchaseModelAssembler purchaseModelAssembler;


    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PurchaseResponse>>> list(
            Pageable pageable,
            @ModelAttribute   PurchaseFilterRequest filter,
            PagedResourcesAssembler<PurchaseResponse> assembler
    ) {

        Page<PurchaseResponse> page = purchaseService.getAllPurchases(pageable, filter);

        PagedModel<EntityModel<PurchaseResponse>> pagedModel = assembler
                .toModel(page, purchaseModelAssembler);

        return ResponseEntity.ok(pagedModel);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
