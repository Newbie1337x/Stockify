package org.stockify.controller.store;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.StoreRequest;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.dto.response.StoreResponse;
import org.stockify.model.assembler.ProductStoreModelAssembler;
import org.stockify.model.assembler.StoreModelAssembler;
import org.stockify.model.service.StockService;
import org.stockify.model.service.StoreService;
import java.net.URI;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StockService stockService;
    private final StoreService storeService;
    private final StoreModelAssembler storeModelAssembler;
    private final ProductStoreModelAssembler productStoreModelAssembler;

    public StoreController(
            StockService stockService, 
            StoreService storeService,
            StoreModelAssembler storeModelAssembler,
            ProductStoreModelAssembler productStoreModelAssembler) {
        this.stockService = stockService;
        this.storeService = storeService;
        this.storeModelAssembler = storeModelAssembler;
        this.productStoreModelAssembler = productStoreModelAssembler;
    }

    // Store endpoints
    @Operation(summary = "List all stores")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<StoreResponse>>> getAllStores(
            Pageable pageable,
            PagedResourcesAssembler<StoreResponse> assembler) {
        Page<StoreResponse> stores = storeService.findAll(pageable);
        PagedModel<EntityModel<StoreResponse>> pagedModel = assembler.toModel(stores, storeModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get store by ID")
    @GetMapping("/{storeID}")
    public ResponseEntity<EntityModel<StoreResponse>> getStoreById(@PathVariable Long storeID) {
        StoreResponse store = storeService.findById(storeID);
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
    @PutMapping("/{storeID}")
    public ResponseEntity<EntityModel<StoreResponse>> updateStore(@PathVariable Long storeID, @Valid @RequestBody StoreRequest request) {
        StoreResponse store = storeService.update(storeID, request);
        return ResponseEntity.ok(storeModelAssembler.toModel(store));
    }

    @Operation(summary = "Delete store by ID")
    @DeleteMapping("/{storeID}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeID) {
        storeService.deleteById(storeID);
        return ResponseEntity.noContent().build();
    }


}
