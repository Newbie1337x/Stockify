package org.stockify.controller.provider;

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
@RequestMapping("/providers/{providerID}/products")
@RequiredArgsConstructor
public class ProviderProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;
    private final ProviderModelAssembler providerModelAssembler;
    private final ProviderService providerService;


    @Operation(summary = "List products associated with a provider")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @PathVariable Long providerID,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> productPage = productService.findProductsByProviderId(providerID, pageable);
        return ResponseEntity.ok(assembler.toModel(productPage, productModelAssembler));
    }

    @Operation(summary = "Assign a specific product to a provider")
    @PutMapping("/{productID}")
    public ResponseEntity<EntityModel<ProviderResponse>> assignProduct(
            @PathVariable Long providerID,
            @PathVariable Long productID
    ){
        return ResponseEntity.ok(providerModelAssembler
                .toModel(providerService.assignProductToProvider(providerID,productID)));
    }

    @Operation(summary = "Unassign a specific product from a provider")
    @PatchMapping("/{productID}")
    public ResponseEntity<EntityModel<ProviderResponse>> unassignProducts(
            @PathVariable Long providerID,
            @PathVariable Long productID
    )
    {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.unassignProductToProvider(providerID,productID)));
    }
}
