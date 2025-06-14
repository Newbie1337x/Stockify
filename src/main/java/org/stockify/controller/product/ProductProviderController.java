package org.stockify.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
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
public class ProductProviderController {

    private final ProductModelAssembler productModelAssembler;
    private final ProductService productService;
    private final ProviderService providerService;
    private final ProviderModelAssembler providerModelAssembler;


    @Operation(summary = "Assign a provider to a product")
    @PutMapping("{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> assignProvider(
            @PathVariable Long providerID,
            @PathVariable Long productID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .assignProviderToProduct(productID,providerID)));
    }


    @Operation(summary = "Unassign a provider from a product")
    @DeleteMapping("{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> unassignProvider(
            @PathVariable Long productID,
            @PathVariable Long providerID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .unassignProviderFromProduct(productID,providerID)));
    }

    @Operation(summary = "List all providers associated with a specific product")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @PathVariable Long productID,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler
    ){
        Page<ProviderResponse> providersPage = providerService.findAllProvidersByProductID(productID, pageable);
        PagedModel<EntityModel<ProviderResponse>> pagedModel = assembler.toModel(providersPage, providerModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }
}
