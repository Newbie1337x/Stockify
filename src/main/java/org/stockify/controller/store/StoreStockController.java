package org.stockify.controller.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.stock.StockRequest;
import org.stockify.dto.request.stock.StockTransferRequest;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.assembler.StockModelAssembler;
import org.stockify.model.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeID}/stock")
@RequiredArgsConstructor
@Tag(name = "StoreStock", description = "Operations related to managing stock at store level")
@SecurityRequirement(name = "bearerAuth")
public class StoreStockController {

    private final StockService stockService;
    private final StockModelAssembler stockModelAssembler;

    @Operation(summary = "Add stock for a specific product and store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock added successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Product or Store not found")
    })
    @PostMapping("{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<StockResponse>> addStock(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the product") @PathVariable Long productID,
            @Valid @RequestBody StockRequest request) {

        StockResponse stock = stockService.addStock(productID, storeID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Update stock for a specific product and store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @PutMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<StockResponse>> updateStock(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the product") @PathVariable Long productID,
            @Valid @RequestBody StockRequest request) {

        StockResponse stock = stockService.updateStock(productID, storeID, request);
        return ResponseEntity.ok(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Get stock for a specific product in a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @GetMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<StockResponse>> getStock(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the product") @PathVariable Long productID) {

        StockResponse stock = stockService.getStock(productID, storeID);
        return ResponseEntity.ok(stockModelAssembler.toModel(stock));
    }

    @Operation(summary = "Remove stock for a specific product and store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock removed successfully"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @DeleteMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    public ResponseEntity<Void> removeStock(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the product") @PathVariable Long productID) {

        stockService.removeStock(productID, storeID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Transfer stock to another store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "One or more stores/products not found")
    })
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<CollectionModel<StockResponse>> transferStock(
            @Parameter(description = "ID of the origin store") @PathVariable Long storeID,
            @Valid @RequestBody StockTransferRequest stockTransferRequest) {

        List<StockResponse> responses = stockService.transferStock(storeID, stockTransferRequest);
        return ResponseEntity.ok(CollectionModel.of(responses));
    }
}
