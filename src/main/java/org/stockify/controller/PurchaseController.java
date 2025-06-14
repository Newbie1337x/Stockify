package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.assembler.PurchaseModelAssembler;
import org.stockify.model.service.PurchaseService;

@RestController
@RequestMapping("/purchases")
@Tag(name = "Purchases", description = "API REST for managing purchase transactions")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PurchaseModelAssembler purchaseModelAssembler;

    @Operation(
        summary = "List all purchases with optional filters",
        description = "Get a paginated list of purchases with optional filters. Returns a list with HATEOAS links."
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Purchases retrieved successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "204", description = "No purchases available")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PurchaseResponse>>> list(
            @Parameter(hidden = true) Pageable pageable,
            @ParameterObject PurchaseFilterRequest filter,
            PagedResourcesAssembler<PurchaseResponse> assembler
    ) {
        Page<PurchaseResponse> page = purchaseService.getAllPurchases(pageable, filter);

        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PagedModel<EntityModel<PurchaseResponse>> pagedModel = assembler.toModel(page, purchaseModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @Operation(
        summary = "Delete a purchase by ID",
        description = "Delete a purchase transaction by its ID. Returns no content on successful deletion."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the purchase to delete", required = true) @PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
