package org.stockify.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.sale.SaleFilterRequest;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.model.assembler.SaleModelAssembler;
import org.stockify.model.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Schema;



@RestController
@RequestMapping("/sales")
@Validated
@Tag(name = "Sale", description = "Endpoints for managing sales")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SaleController {
    private final SaleService saleService;
    private final SaleModelAssembler saleModelAssembler;

    @Operation(
            summary = "Get paged list of sales",
            description = "Returns a paginated list of sales, optionally filtered",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paged list of sales retrieved", content = @Content(schema = @Schema(implementation = PagedModel.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<SaleResponse>>> getAll(
            @Parameter(description = "Filter request object")
            @ParameterObject SaleFilterRequest filterRequest,

            @Parameter(description = "Page number (0..N)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size,
            PagedResourcesAssembler<SaleResponse> assembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<SaleResponse> saleResponsePage = saleService.findAll(filterRequest, pageable);

        return ResponseEntity.ok(assembler.toModel(saleResponsePage, saleModelAssembler));
    }

    @Operation(
            summary = "Get sale by ID",
            description = "Returns a single sale by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale found", content = @Content(schema = @Schema(implementation = SaleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
            }
    )
    @GetMapping("/{saleID}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<EntityModel<SaleResponse>> getSaleById(
            @Parameter(description = "Sale ID", required = true, example = "1")
            @PathVariable Long saleID) {
        SaleResponse saleResponse = saleService.findById(saleID);
        return ResponseEntity.ok(saleModelAssembler.toModel(saleResponse));
    }




    @Operation(
            summary = "Delete a sale by ID",
            description = "Deletes a sale given its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sale deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
            }
    )
    @DeleteMapping("/{saleID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    public ResponseEntity<Void> deleteSaleById(
            @Parameter(description = "Sale ID", required = true, example = "1")
            @PathVariable Long saleID) {
        saleService.delete(saleID);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update entire sale by ID",
            description = "Performs full update of a sale given its ID",
            requestBody = @RequestBody(
                    description = "Sale update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SaleRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale updated successfully", content = @Content(schema = @Schema(implementation = SaleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
            }
    )
    @PutMapping("/{saleID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<SaleResponse>> putSale(
            @Parameter(description = "Sale ID", required = true, example = "1")
            @PathVariable Long saleID,

            @Parameter(description = "Sale request body", required = true)
            @Valid @RequestBody SaleRequest saleRequest) {
        SaleResponse updatedSale = saleService.updateSaleFull(saleID, saleRequest);
        EntityModel<SaleResponse> entityModel = saleModelAssembler.toModel(updatedSale);
        return ResponseEntity.ok(entityModel);
    }

    @Operation(
            summary = "Partially update a sale by ID",
            description = "Performs partial update of a sale given its ID",
            requestBody = @RequestBody(
                    description = "Partial sale update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SaleRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale partially updated successfully", content = @Content(schema = @Schema(implementation = SaleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
            }
    )
    @PatchMapping("/{saleID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<SaleResponse>> patchSale(
            @Parameter(description = "Sale ID", required = true, example = "1")
            @PathVariable Long saleID,

            @Parameter(description = "Sale request body", required = true)
            @Valid @RequestBody SaleRequest saleRequest) {
        SaleResponse updatedSale = saleService.updateShiftPartial(saleID, saleRequest);
        EntityModel<SaleResponse> entityModel = saleModelAssembler.toModel(updatedSale);
        return ResponseEntity.ok(entityModel);
    }
}
