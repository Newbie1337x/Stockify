package org.stockify.controller.store;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.stock.StockRequest;
import org.stockify.dto.request.stock.StockTransferRequest;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.assembler.StockModelAssembler;
import org.stockify.model.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeID}/stock")
public class StoreStockController {

    private final StockService stockService;
    private final StockModelAssembler stockModelAssembler;

    public StoreStockController(StockService stockService, StockModelAssembler stockModelAssembler) {
        this.stockService = stockService;
        this.stockModelAssembler = stockModelAssembler;
    }

 

    @Operation(summary = "Add a stock for a specific product and store")
    @PostMapping("{productID}")
    public ResponseEntity<EntityModel<StockResponse>> addStock(
            @PathVariable Long storeID,
            @PathVariable Long productID,
            @Valid @RequestBody StockRequest request
    ) {
        StockResponse stock = stockService.addStock(productID,storeID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Update stock for a specific product and store")
    @PutMapping("/{productID}")
    public ResponseEntity<EntityModel<StockResponse>> updateStock(
            @PathVariable Long storeID,
            @PathVariable Long productID,
            @Valid @RequestBody StockRequest request
    ) {
        StockResponse stock = stockService.updateStock(productID, storeID, request);
        return ResponseEntity.ok(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Get stock for a specific product in a store")
    @GetMapping("/{productID}")
    public ResponseEntity<EntityModel<StockResponse>> getStock(
            @PathVariable Long storeID,
            @PathVariable Long productID
    ) {
        StockResponse stock = stockService.getStock(productID, storeID);
        return ResponseEntity.ok(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Remove stock for a specific product and store")
    @DeleteMapping("/{productID}")
    public ResponseEntity<Void> removeStock(
            @PathVariable Long storeID,
            @PathVariable Long productID
    ) {
        stockService.removeStock(productID, storeID);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Transfer quantity to other store")
    @PostMapping("/transfer")
    public ResponseEntity<CollectionModel<StockResponse>> transferStock(
            @RequestBody StockTransferRequest stockTransferRequest,
            @PathVariable Long storeID
    ) {
        List<StockResponse> responses = stockService.transferStock(storeID, stockTransferRequest);
        return ResponseEntity.ok(CollectionModel.of(responses));
    }

}

