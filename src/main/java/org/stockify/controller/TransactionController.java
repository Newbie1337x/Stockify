package org.stockify.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.service.TransactionService;

@RestController
@RequestMapping("/store/{idLocal}/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping("/{idPos}")
    public void createTransaction(@PathVariable Long idLocal,
                                  @PathVariable Long idPos,@RequestBody @Valid TransactionRequest request)
    {
        transactionService.createTransaction(request, idLocal, idPos, TransactionType.OTHER);
    }

}
