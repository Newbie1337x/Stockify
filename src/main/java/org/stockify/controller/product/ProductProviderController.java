package org.stockify.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productID}/providers")
@Tag(name = "ProductProvider", description = "Operations for managing product-provider relationships")
@SecurityRequirement(name = "bearerAuth")
public class ProductProviderController {

    private final ProductModelAssembler productModelAssembler;
    private final ProductService productService;
    private final ProviderService providerService;
    private final ProviderModelAssembler providerModelAssembler;

    @Operation(summary = "Assign a provider to a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Product or Provider not found"),
            @ApiResponse(responseCode = "409", description = "Provider already assigned to product")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    @PutMapping("{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> assignProvider(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID,
            @Parameter(description = "ID of the product") @PathVariable Long productID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .assignProviderToProduct(productID, providerID)));
    }

    @Operation(summary = "Unassign a provider from a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider unassigned successfully"),
            @ApiResponse(responseCode = "404", description = "Product or Provider not found")
    })
    @DeleteMapping("{providerID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    public ResponseEntity<EntityModel<ProductResponse>> unassignProvider(
            @Parameter(description = "ID of the product") @PathVariable Long productID,
            @Parameter(description = "ID of the provider") @PathVariable Long providerID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .unassignProviderFromProduct(productID, providerID)));
    }

    @Operation(summary = "List all providers associated with a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of providers returned successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @Parameter(description = "ID of the product") @PathVariable Long productID,
            @Parameter(hidden = true)
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler
    ){
        Page<ProviderResponse> providersPage = providerService.findAllProvidersByProductID(productID, pageable);
        return ResponseEntity.ok(assembler.toModel(providersPage, providerModelAssembler));
    }
}
