package org.stockify.controller;
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
import org.stockify.dto.request.AssignProvidersRequest;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;
    private final PagedResourcesAssembler<ProductResponse> pagedResourcesAssembler;
    private final ProviderModelAssembler providerModelAssembler;
    private final ProviderService providerService;

    public ProductController(ProductService productService, ProductModelAssembler productModelAssembler, PagedResourcesAssembler<ProductResponse> pagedResourcesAssembler, ProviderModelAssembler providerModelAssembler, ProviderService providerService) {
        this.productService = productService;
        this.productModelAssembler = productModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.providerModelAssembler = providerModelAssembler;
        this.providerService = providerService;
    }


    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @RequestParam(required = false) Set<String> categories,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ProductResponse> products;

        if (categories == null || categories.isEmpty()) {
            products = productService.findAll(pageable);
        } else {
            products = productService.filterByCategories(categories, pageable);
        }

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        PagedModel<EntityModel<ProductResponse>> pagedModel = pagedResourcesAssembler.toModel(products, productModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }


    @PostMapping("/bulk")
    public ResponseEntity<BulkProductResponse> bulkSaveProducts(@RequestBody List<ProductRequest> products) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(productService.saveAll(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.save(product)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.update(id, product)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> patchProduct(@PathVariable int id,@RequestBody ProductRequest product){

        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.patch(id,product)));
    }




    //Providers logic

    @PutMapping("/{productID}/providers/{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> assignProvider(
            @PathVariable int productID,
            @PathVariable Long providerID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .assignProviderToProduct(productID,providerID)));
    }



    @DeleteMapping("/{productID}/providers/{providerID}")
    public ResponseEntity<EntityModel<ProductResponse>> unassignProvider(
            @PathVariable int productID,
            @PathVariable Long providerID
    ){
        return ResponseEntity.ok(productModelAssembler
                .toModel(productService
                        .unassignProviderFromProduct(productID,providerID)));
    }


    @GetMapping("/{id}/providers")
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @PathVariable int id,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler
    ){
        Page<ProviderResponse> providersPage = providerService.findAllProvidersByProductID(id, pageable);
        PagedModel<EntityModel<ProviderResponse>> pagedModel = assembler.toModel(providersPage, providerModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }





}
