package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ProviderFilterRequest;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.BulkProviderResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService providerService;
    private final ProductService productService;
    private final ProviderModelAssembler providerModelAssembler;
    private final ProductModelAssembler productModelAssembler;

    public ProviderController(ProviderService providerService,
                              ProductService productService,
                              ProviderModelAssembler providerModelAssembler,
                              ProductModelAssembler productModelAssembler) {

        this.providerService = providerService;
        this.productService = productService;
        this.providerModelAssembler = providerModelAssembler;
        this.productModelAssembler = productModelAssembler;
    }

    //---Crud operations---
    @Operation(summary = "List all providers with optional filters")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @Valid ProviderFilterRequest filters,
            @PageableDefault(sort = "name") Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler) {

        Page<ProviderResponse> providerPage = providerService.findAll(pageable, filters);
        PagedModel<EntityModel<ProviderResponse>> pagedModel =
                assembler.toModel(providerPage, providerModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get provider by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProviderResponse>> getProviderById(@PathVariable Long id) {
        ProviderResponse providerResponse = providerService.findById(id);
        EntityModel<ProviderResponse> entityModel = providerModelAssembler.toModel(providerResponse);
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Create a new provider")
    @PostMapping
        public ResponseEntity<EntityModel<ProviderResponse>> createProvider(@Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.save(request)));
    }

    @Operation(summary = "Create multiple providers")
    @PostMapping("/bulk")
    public ResponseEntity<BulkProviderResponse> bulkSaveProviders(@Valid @RequestBody List<@Valid ProviderRequest> providers) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.saveAll(providers));
    }

    @Operation(summary = "Logically delete a provider")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<EntityModel<ProviderResponse>> logicalDeleteProvider(@PathVariable Long id) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.logicalDelete(id)));
    }

    @Operation(summary = "Delete a provider from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<ProviderResponse>> deleteProvider(@PathVariable Long id) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.logicalDelete(id)));
    }


    //Products Logic

    @Operation(summary = "List products associated with a provider")
    @GetMapping("/{id}/products")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @PathVariable Long id,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> productPage = productService.findProductsByProviderId(id, pageable);
        return ResponseEntity.ok(assembler.toModel(productPage, productModelAssembler));
    }

    @Operation(summary = "Assign a specific product to a provider")
    @PutMapping("/{providerID}/products/{productID}")
    public ResponseEntity<EntityModel<ProviderResponse>> assignProduct(
            @PathVariable Long providerID,
            @PathVariable Long productID
    ){
        return ResponseEntity.ok(providerModelAssembler
                .toModel(providerService.assignProductToProvider(providerID,productID)));
    }

    @Operation(summary = "Unassign a specific product from a provider")
    @PatchMapping("/{providerID}/products/{productID}")
    public ResponseEntity<EntityModel<ProviderResponse>> unassignProducts(
            @PathVariable Long providerID,
            @PathVariable Long productID
    )
    {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.unassignProductToProvider(providerID,productID)));
    }
}
