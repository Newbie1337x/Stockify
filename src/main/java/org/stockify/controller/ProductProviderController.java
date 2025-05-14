package org.stockify.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.AssignProvidersRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.service.ProductService;

@RestController
@RequestMapping("/api/products")

public class ProductProviderController {

    private final ProductService productService;

    public ProductProviderController(ProductService productService) {
        this.productService = productService;
    }

    @PutMapping("/{id}/providers")
    public ResponseEntity<ProductResponse> assignProviders(
            @PathVariable int id,
            @RequestBody AssignProvidersRequest request
    ){

    return ResponseEntity.ok(productService.assignProviderToProduct(id,request.getProvidersIds()));
    }

}
