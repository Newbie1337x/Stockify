package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PurchaseModelAssembler purchaseModelAssembler;

    @Operation(summary = "Listar todas las compras con filtros opcionales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras obtenidas exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay compras disponibles")
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

    @Operation(summary = "Eliminar una compra por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la compra a eliminar") @PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
