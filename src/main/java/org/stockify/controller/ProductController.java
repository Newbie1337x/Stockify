package org.stockify.controller;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;
    private final ProviderModelAssembler providerModelAssembler;
    private final ProviderService providerService;

    public ProductController(ProductService productService,
                             ProductModelAssembler productModelAssembler,
                             ProviderModelAssembler providerModelAssembler,
                             ProviderService providerService) {
        this.productService = productService;
        this.productModelAssembler = productModelAssembler;
        this.providerModelAssembler = providerModelAssembler;
        this.providerService = providerService;
    }

    @Operation(summary = "List all products with optional filters")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            ProductFilterRequest filter,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> products = productService.findAll(pageable,filter);

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PagedModel<EntityModel<ProductResponse>> pagedModel = assembler.toModel(products, productModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Create multiple products in bulk")
    @PostMapping("/bulk")
    public ResponseEntity<BulkProductResponse> bulkSaveProducts(@RequestBody List<ProductRequest> products) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(productService.saveAll(products));
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.findById(id)));
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<EntityModel<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.save(product)));
    }

    @Operation(summary = "Delete a product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an existing product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.update(id, product)));
    }

    @Operation(summary = "Patch an existing product by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> patchProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.patch(id,product)));
    }


    //Providers logic

    @Operation(summary = "Assign a provider to a product")
    @PutMapping("/{productID}/providers/{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> assignProvider(
            @PathVariable Long productID,
            @PathVariable Long providerID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .assignProviderToProduct(productID,providerID)));
    }


    @Operation(summary = "Unassign a provider from a product")
    @DeleteMapping("/{productID}/providers/{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> unassignProvider(
            @PathVariable Long productID,
            @PathVariable Long providerID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .unassignProviderFromProduct(productID,providerID)));
    }

    @Operation(summary = "List all providers associated with a specific product")
    @GetMapping("/{id}/providers")
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @PathVariable Long id,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler
    ){
        Page<ProviderResponse> providersPage = providerService.findAllProvidersByProductID(id, pageable);
        PagedModel<EntityModel<ProviderResponse>> pagedModel = assembler.toModel(providersPage, providerModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }
}
