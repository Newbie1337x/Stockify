package org.stockify.service;


import org.springframework.stereotype.Service;
import org.stockify.repository.StockRepository;

@Service
public class StockService {
    private final StockRepository stockRepository;

    StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }
}
