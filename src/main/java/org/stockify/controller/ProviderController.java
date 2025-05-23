package org.stockify.controller;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.AssignProductRequest;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;

import java.util.List;


@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final ProductService productService;

    public ProviderController(ProviderService providerService, ProductService productService) {
        this.providerService = providerService;
        this.productService = productService;
    }

    //---Crud operations---
    @GetMapping
    public ResponseEntity<Page<ProviderResponse>> listProviders(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok().body(providerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.findById(id));
    }

    @PostMapping
        public ResponseEntity<ProviderResponse> createProvider(@Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerService.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ProviderResponse>> bulkSaveProviders(@Valid @RequestBody List<@Valid ProviderRequest> providers) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.saveAll(providers));
    }
    /*
    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerService.update(request));
        }
    */

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> logicalDeleteProvider(@PathVariable Long id) {
        providerService.logicalDelete(id);
        return ResponseEntity.ok("Proveedor dado de baja correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.ok("Proveedor eliminado correctamente");
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Page<ProductResponse>> listProducts(
            @PathVariable Long id,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findProductsByProviderId(id, pageable));
    }

    @PutMapping("/{id}/products")
    public ResponseEntity<ProviderResponse> assingProducts(
            @PathVariable Long id,
            @RequestBody AssignProductRequest request
    ){
        return ResponseEntity.ok(providerService.assignProductsToProvider(id,request.getProductsId()));
    }

    //FILTERS //CONSULTAR

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String businessName,
            @RequestParam(required = false) String taxId,
            Pageable pageable) {

        if (name != null) {
            return ResponseEntity.ok(providerService.findByName(pageable, name));
        } else if (businessName != null) {
            return ResponseEntity.ok(providerService.findByBusinessName(businessName));
        } else if (taxId != null) {
            return ResponseEntity.ok(providerService.findByTaxId(taxId));
        } else {
            return listProviders(pageable);
        }
    }
}
