package org.stockify.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.sale.SaleFilterRequest;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.assembler.SaleModelAssembler;
import org.stockify.model.service.SaleService;

@RestController
 @RequestMapping("/sales")
public class SaleController {
    private final SaleService saleService;
    private final SaleModelAssembler saleModelAssembler;

    public SaleController(SaleService saleService, SaleModelAssembler saleModelAssembler) {
        this.saleService = saleService;
        this.saleModelAssembler = saleModelAssembler;
    }

     @PostMapping(("/{storeID}/{posID}" ) )
     public ResponseEntity<EntityModel<SaleResponse>> create(@Valid @RequestBody SaleRequest request, @PathVariable Long storeID, @PathVariable Long posID) {
        SaleResponse saleResponse = saleService.createSale(request,storeID,posID);
        EntityModel<SaleResponse> entityModel = saleModelAssembler.toModel(saleResponse);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
     }

     @GetMapping
     public ResponseEntity<PagedModel<EntityModel<SaleResponse>>> getAll(
             @ModelAttribute SaleFilterRequest filterRequest,
             @RequestParam(required = false, defaultValue = "0") int page,
             @RequestParam(required = false, defaultValue = "20") int size,
             PagedResourcesAssembler<SaleResponse> assembler) {

         Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
         Page<SaleResponse> saleResponsePage = saleService.findAll(filterRequest, pageable);

         return ResponseEntity.ok(assembler.toModel(saleResponsePage, saleModelAssembler));
     }

     @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SaleResponse>> getSaleById(@PathVariable Long id) {
        SaleResponse saleResponse = saleService.findbyId(id);

        return ResponseEntity.ok(saleModelAssembler.toModel(saleResponse));
     }

     @GetMapping("/{id}/transction")
    public ResponseEntity<TransactionResponse> getSaleTransctionById(@PathVariable Long id) {
         TransactionResponse transactionResponse = saleService.findTransactionBySaleId(id);

         return ResponseEntity.ok(transactionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleById(@PathVariable Long id) {
        saleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SaleResponse>> putSale(@PathVariable Long id, @Valid @RequestBody SaleRequest saleRequest) {
        SaleResponse updatedSale = saleService.updateSaleFull(id, saleRequest);
        EntityModel<SaleResponse> entityModel = saleModelAssembler.toModel(updatedSale);

        return ResponseEntity.ok(entityModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<SaleResponse>> patchSale(@PathVariable Long id, @Valid @RequestBody SaleRequest saleRequest) {
        SaleResponse updatedSale = saleService.updateShiftPartial(id, saleRequest);
        EntityModel<SaleResponse> entityModel = saleModelAssembler.toModel(updatedSale);

        return ResponseEntity.ok(entityModel);
    }
 }
