package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.stock.StockRequest;
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


}
