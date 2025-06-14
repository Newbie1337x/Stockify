package org.stockify.controller.transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionPDFResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.service.TransactionService;

@RestController
@RequestMapping
@RequiredArgsConstructor

//Implement Hateoas and REST
public class TransactionController {
    private final TransactionService transactionService;


    @PostMapping("/stores/{storeID}/pos/{posID}/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable Long storeID,
                                            @PathVariable Long posID, @RequestBody @Valid TransactionRequest request)
    {
        return ResponseEntity.ok(transactionService.createTransaction(request, storeID, posID, TransactionType.OTHER));
    }
    
    @GetMapping("transaction/pdf/{idTransaction}")
    public ResponseEntity<EntityModel<TransactionPDFResponse>> generatePdf(@PathVariable Long idTransaction) throws Exception {
        return transactionService.generatePdf(idTransaction);
    }


}
