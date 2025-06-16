package org.stockify.controller.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.product.ProductFilterRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.model.assembler.ProductStoreModelAssembler;
import org.stockify.model.service.StockService;

@RestController
@RequestMapping("/stores/{storeID}/products")
@RequiredArgsConstructor
@Tag(name = "StoreProduct", description = "Operations for managing store-specific product listings")
public class StoreProductController {

    private final StockService stockService;
    private final ProductStoreModelAssembler productStoreModelAssembler;

    @Operation(summary = "List all products from a specific store which match the filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of products from the store returned successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductStoreResponse>>> getProductsFromStore(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(hidden = true) Pageable pageable,
            PagedResourcesAssembler<ProductStoreResponse> assembler,
            @ParameterObject ProductFilterRequest filter) {

        Page<ProductStoreResponse> products = stockService.listProductsByStore(storeID, pageable, filter);
        return ResponseEntity.ok(assembler.toModel(products, productStoreModelAssembler));
    }

    @Operation(summary = "Get a specific product from a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found in the store"),
            @ApiResponse(responseCode = "404", description = "Product or store not found")
    })
    @GetMapping("{productID}")
    public ResponseEntity<EntityModel<ProductStoreResponse>> getProductFromStore(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Parameter(description = "ID of the product") @PathVariable Long productID) {

        ProductStoreResponse response = stockService.getProductByStoreAndProductID(storeID, productID);
        return ResponseEntity.ok(productStoreModelAssembler.toModel(response));
    }
}
