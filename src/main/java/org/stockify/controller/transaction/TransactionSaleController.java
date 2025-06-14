package org.stockify.controller.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.model.assembler.SaleModelAssembler;
import org.stockify.model.service.SaleService;

@RestController
@RequestMapping("/stores/{storeID}/pos/{posID}/transactions/sales")
@RequiredArgsConstructor
@Tag(name = "Sales", description = "Endpoints for managing sales transactions")
public class TransactionSaleController {

    private final SaleService saleService;
    private final SaleModelAssembler saleModelAssembler;

    @Operation(
            summary = "Create a new sale",
            description = "Creates a sale for a given store and POS"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale created successfully",
                    content = @Content(schema = @Schema(implementation = SaleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Store or POS not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<SaleResponse>> create(
            @Parameter(description = "Sale request body", required = true)
            @Valid @RequestBody SaleRequest request,

            @Parameter(description = "ID of the store", required = true, example = "1")
            @PathVariable Long storeID,

            @Parameter(description = "ID of the POS", required = true, example = "10")
            @PathVariable Long posID) {

        SaleResponse saleResponse = saleService.createSale(request, storeID, posID);
        EntityModel<SaleResponse> entityModel = saleModelAssembler.toModel(saleResponse);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }
}
