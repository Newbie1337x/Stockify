package org.stockify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.service.ProductService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products/{productId}/categories")
public class ProductCategoryController {

    private final ProductService productService;

    public ProductCategoryController(ProductService productService) {
        this.productService = productService;
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ProductResponse> removeCategoryFromProduct(
            @PathVariable int productId,
            @PathVariable int categoryId) {
        return ResponseEntity.ok(productService.deleteCategoryFromProduct(categoryId, productId));
    }

    @DeleteMapping
    public ResponseEntity<ProductResponse> removeAllCategoriesFromProduct(
            @PathVariable int productId) {
        return ResponseEntity.ok(productService.deleteAllCategoryFromProduct(productId));
    }

    @GetMapping
    public ResponseEntity<Set<CategoryResponse>> getCategoriesFromProduct(
            @PathVariable int productId) {
        return ResponseEntity.ok(productService.findCategoriesByProductId(productId));
    }
}
