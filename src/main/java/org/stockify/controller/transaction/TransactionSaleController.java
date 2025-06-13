package org.stockify.controller.transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.model.assembler.SaleModelAssembler;
import org.stockify.model.service.SaleService;

@RestController
@RequestMapping("stores/{storeID}/pos/{posID}/transactions/sales")
public class TransactionSaleController {

    private final SaleService saleService;


    public TransactionSaleController(SaleService saleService, SaleModelAssembler saleModelAssembler) {
        this.saleService = saleService;

    }

    @PostMapping
    @Transactional
    public ResponseEntity<SaleResponse> create(

            @Valid @RequestBody SaleRequest request,
            @PathVariable Long storeID,
            @PathVariable Long posID) {
        System.out.println("Request: " + request);
        SaleResponse saleResponse = saleService.createSale(request, storeID, posID);
        return new ResponseEntity<>(saleResponse,HttpStatus.CREATED);
    }




}
