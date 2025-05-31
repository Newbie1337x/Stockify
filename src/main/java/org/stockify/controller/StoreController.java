package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.StockRequest;
import org.stockify.dto.request.StoreRequest;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.dto.response.StockResponse;
import org.stockify.dto.response.StoreResponse;
import org.stockify.model.assembler.ProductStoreModelAssembler;
import org.stockify.model.assembler.StockModelAssembler;
import org.stockify.model.assembler.StoreModelAssembler;
import org.stockify.model.service.StockService;
import org.stockify.model.service.StoreService;

import java.net.URI;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StockService stockService;
    private final StoreService storeService;
    private final StoreModelAssembler storeModelAssembler;
    private final StockModelAssembler stockModelAssembler;
    private final ProductStoreModelAssembler productStoreModelAssembler;

    public StoreController(
            StockService stockService, 
            StoreService storeService,
            StoreModelAssembler storeModelAssembler,
            StockModelAssembler stockModelAssembler,
            ProductStoreModelAssembler productStoreModelAssembler) {
        this.stockService = stockService;
        this.storeService = storeService;
        this.storeModelAssembler = storeModelAssembler;
        this.stockModelAssembler = stockModelAssembler;
        this.productStoreModelAssembler = productStoreModelAssembler;
    }

    // Store endpoints
    @Operation(summary = "List all stores")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<StoreResponse>>> getAllStores(
            Pageable pageable,
            PagedResourcesAssembler<StoreResponse> assembler) {
        Page<StoreResponse> stores = storeService.findAll(pageable);
        if (stores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PagedModel<EntityModel<StoreResponse>> pagedModel = assembler.toModel(stores, storeModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get store by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<StoreResponse>> getStoreById(@PathVariable Long id) {
        StoreResponse store = storeService.findById(id);
        return ResponseEntity.ok(storeModelAssembler.toModel(store));
    }

    @Operation(summary = "Create a new store")
    @PostMapping
    public ResponseEntity<EntityModel<StoreResponse>> createStore(@Valid @RequestBody StoreRequest request) {
        StoreResponse store = storeService.save(request);
        EntityModel<StoreResponse> storeModel = storeModelAssembler.toModel(store);
        return ResponseEntity.created(URI.create("/api/stores/" + store.id())).body(storeModel);
    }

    @Operation(summary = "Update store by ID")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<StoreResponse>> updateStore(@PathVariable Long id, @Valid @RequestBody StoreRequest request) {
        StoreResponse store = storeService.update(id, request);
        return ResponseEntity.ok(storeModelAssembler.toModel(store));
    }

    @Operation(summary = "Delete store by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all products from a specific store")
    @GetMapping("/{id}/products")
    public ResponseEntity<PagedModel<EntityModel<ProductStoreResponse>>> getProductsFromStore(
            @PathVariable Long id, 
            Pageable pageable,
            PagedResourcesAssembler<ProductStoreResponse> assembler,
            ProductFilterRequest filter) {
        Page<ProductStoreResponse> products = stockService.listProductsByStore(id, pageable, filter);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PagedModel<EntityModel<ProductStoreResponse>> pagedModel = assembler.toModel(products, productStoreModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    // Stock endpoints

    @Operation(summary = "List all products from a specific store with optional filters")
    @PostMapping("/stock")
    public ResponseEntity<EntityModel<StockResponse>> addStock(@Valid @RequestBody StockRequest request) {
        StockResponse stock = stockService.addStock(request);
        EntityModel<StockResponse> stockModel = stockModelAssembler.toModel(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockModel);
    }

    @Operation(summary = "Update stock for a specific product and store or change the product or store related")
    @PutMapping("/stock")
    public ResponseEntity<EntityModel<StockResponse>> updateStock(@Valid @RequestBody StockRequest request) {
        StockResponse stock = stockService.updateStock(request);
        return ResponseEntity.ok(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Get stock for a specific product and store")
    @GetMapping("/stock")
    public ResponseEntity<EntityModel<StockResponse>> getStock(@RequestParam Long productId, @RequestParam Long storeId) {
        StockResponse stock = stockService.getStock(productId, storeId);
        return ResponseEntity.ok(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Remove stock for a specific product and store")
    @DeleteMapping("/stock")
    public ResponseEntity<Void> removeStock(@RequestParam Long productId, @RequestParam Long storeId) {
        stockService.removeStock(productId, storeId);
        return ResponseEntity.noContent().build();
    }
}
