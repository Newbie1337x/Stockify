package org.stockify.controller;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.AssignProvidersRequest;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.service.ProductService;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> listProducts(
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
        return ResponseEntity.ok(products);
    }


    @PostMapping("/bulk")
    public ResponseEntity<BulkProductResponse> bulkSaveProducts(@RequestBody List<ProductRequest> products) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(productService.saveAll(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequest product){

        return ResponseEntity.ok().body(productService.update(id, product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(@PathVariable int id,@RequestBody ProductRequest product){

        return ResponseEntity.ok().body(productService.patch(id,product));
    }

    @PutMapping("/{id}/providers")
    public ResponseEntity<ProductResponse> assignProviders(
            @PathVariable int id,
            @RequestBody AssignProvidersRequest request
    ){

        return ResponseEntity.ok(productService.assignProviderToProduct(id,request.getProvidersIds()));
    }

}
