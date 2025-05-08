package org.stockify.model.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.service.ProductService;
import org.stockify.model.entity.ProductEntity;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        if (productService.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody ProductRequest product){
        productService.update(id, product);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductEntity> patchProduct(@PathVariable int id,@RequestBody ProductRequest product){
        productService.patch(id,product);
        return ResponseEntity.ok().build();
    }

}
