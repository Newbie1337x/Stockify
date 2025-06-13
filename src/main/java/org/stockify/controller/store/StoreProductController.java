package org.stockify.controller.store;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.model.assembler.ProductStoreModelAssembler;
import org.stockify.model.service.StockService;

@RestController
@RequestMapping("/stores/{storeID}/products")
public class StoreProductController {

    private final StockService stockService;
    private final ProductStoreModelAssembler productStoreModelAssembler;
    public StoreProductController(StockService stockService, ProductStoreModelAssembler productStoreModelAssembler) {
        this.stockService = stockService;
        this.productStoreModelAssembler = productStoreModelAssembler;
    }

    @Operation(summary = "List all products from a specific store which match the filter criteria")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductStoreResponse>>> getProductsFromStore(
            @PathVariable Long storeID,
            Pageable pageable,
            PagedResourcesAssembler<ProductStoreResponse> assembler,
            @ModelAttribute(name = "filter", binding = false)
            ProductFilterRequest filter) {
        Page<ProductStoreResponse> products = stockService.listProductsByStore(storeID, pageable, filter);

        PagedModel<EntityModel<ProductStoreResponse>> pagedModel = assembler.toModel(products, productStoreModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("{productID}")
    public ResponseEntity<EntityModel<ProductStoreResponse>> getProductFromStore(
            @PathVariable Long storeID,
            @PathVariable Long productID
    ) {
        ProductStoreResponse response = stockService.getProductByStoreAndProductID(storeID, productID);
        return ResponseEntity.ok(productStoreModelAssembler.toModel(response));
    }






}
