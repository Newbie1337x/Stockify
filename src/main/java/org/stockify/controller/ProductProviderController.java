package org.stockify.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.AssignProductRequest;
import org.stockify.dto.request.AssignProvidersRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;

@RestController
@RequestMapping("/api/products")

public class ProductProviderController {

    private final ProductService productService;
    private final ProviderService providerService;

    public ProductProviderController(ProductService productService, ProviderService providerService) {
        this.productService = productService;
        this.providerService = providerService;
    }

    @PutMapping("/{id}/providers")
    public ResponseEntity<ProductResponse> assignProviders(
            @PathVariable int id,
            @RequestBody AssignProvidersRequest request
    ){

    return ResponseEntity.ok(productService.assignProviderToProduct(id,request.getProvidersIds()));
    }

    @PutMapping("/{id}/products")
    public ResponseEntity<ProviderResponse> assingProducts(
            @PathVariable Long id,
            @RequestBody AssignProductRequest request
    ){
        return ResponseEntity.ok(providerService.assignProductsToProvider(id,request.getProductsId()));
    }

}
