package org.stockify.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.model.service.SaleService;
 @RestController
 @RequestMapping("/sales")
 @RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;


     @PostMapping(("/{storeID}/{posID}" ) )
     public ResponseEntity<SaleResponse> create(@Valid @RequestBody SaleRequest request, @PathVariable Long storeID, @PathVariable Long posID) {
         return new ResponseEntity<>(saleService.createSale(request, storeID, posID), HttpStatus.CREATED);
     }


 }
