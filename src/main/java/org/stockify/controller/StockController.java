package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.StockRequest;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stock")


public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Transfer quantity to other store")
    @PostMapping("/transfer/{id}")
    public ResponseEntity<List<StockResponse>> transferStock(@RequestBody StockRequest stockRequest, @PathVariable Long id
    ) {
        List<StockResponse> responses = stockService.transferStock(id,stockRequest);
        return ResponseEntity.ok(responses);
    }
}
