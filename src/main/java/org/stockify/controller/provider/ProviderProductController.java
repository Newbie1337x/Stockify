package org.stockify.controller.provider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/providers/{providerID}/products")
@RequiredArgsConstructor
@Tag(name = "ProviderProduct", description = "Operations for managing product-provider relationships")
@SecurityRequirement(name = "bearerAuth")
public class ProviderProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;
    private final ProviderModelAssembler providerModelAssembler;
    private final ProviderService providerService;

    @Operation(summary = "List products associated with a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID,
            @Parameter(hidden = true)
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> productPage = productService.findProductsByProviderId(providerID, pageable);
        return ResponseEntity.ok(assembler.toModel(productPage, productModelAssembler));
    }

    @Operation(summary = "Assign a specific product to a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product assigned to provider successfully"),
            @ApiResponse(responseCode = "404", description = "Product or Provider not found"),
            @ApiResponse(responseCode = "409", description = "Product already assigned to provider")
    })
    @PutMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> assignProduct(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID,
            @Parameter(description = "ID of the product") @PathVariable Long productID
    ){
        return ResponseEntity.ok(providerModelAssembler
                .toModel(providerService.assignProductToProvider(providerID, productID)));
    }

    @Operation(summary = "Unassign a specific product from a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product unassigned from provider successfully"),
            @ApiResponse(responseCode = "404", description = "Product or Provider not found")
    })
    @PatchMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> unassignProducts(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID,
            @Parameter(description = "ID of the product") @PathVariable Long productID
    ){
        return ResponseEntity.ok(
                providerModelAssembler.toModel(
                        providerService.unassignProductToProvider(providerID, productID)
                )
        );
    }
}
