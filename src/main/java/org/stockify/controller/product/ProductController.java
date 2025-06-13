package org.stockify.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.service.ProductService;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;


    public ProductController(ProductService productService,
                             ProductModelAssembler productModelAssembler) {
        this.productService = productService;
        this.productModelAssembler = productModelAssembler;
    }

    @Operation(summary = "List all products with optional filters")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            ProductFilterRequest filter,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> products = productService.findAll(pageable,filter);


        PagedModel<EntityModel<ProductResponse>> pagedModel = assembler.toModel(products, productModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Create multiple products in bulk")
    @PostMapping("/bulk")
    public ResponseEntity<BulkProductResponse> bulkSaveProducts(@RequestBody List<ProductRequest> products) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(productService.saveAll(products));
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{productID}")
    public ResponseEntity<EntityModel<ProductResponse>> getProductById(@PathVariable Long productID) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.findById(productID)));
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<EntityModel<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.save(product)));
    }

    @Operation(summary = "Delete a product by ID")
    @DeleteMapping("/{productID}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productID) {
        productService.deleteById(productID);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an existing product by ID")
    @PutMapping("/{productID}")
    public ResponseEntity<EntityModel<ProductResponse>> updateProduct(@PathVariable Long productID, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.update(productID, product)));
    }

    @Operation(summary = "Patch an existing product by ID")
    @PatchMapping("/{productID}")
    public ResponseEntity<EntityModel<ProductResponse>> patchProduct(@PathVariable Long productID, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.patch(productID,product)));
    }

}
