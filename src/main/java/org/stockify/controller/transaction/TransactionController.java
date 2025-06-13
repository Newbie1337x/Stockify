package org.stockify.controller.transaction;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.service.TransactionService;

@RestController
@RequestMapping("/stores/{storeID}/pos/{posID}/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping
    public void createTransaction(@PathVariable Long storeID,
                                  @PathVariable Long posID,@RequestBody @Valid TransactionRequest request)
    {
        transactionService.createTransaction(request, storeID, posID, TransactionType.OTHER);
    }



}
