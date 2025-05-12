package org.stockify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products/categories")
public class ProductCategoryController {

    private final ProductService productService;

    public ProductCategoryController(ProductService productService) {
        this.productService = productService;
    }


    @DeleteMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ProductResponse> removeCategoryFromProduct(
            @PathVariable int productId,
            @PathVariable int categoryId) {
        return ResponseEntity.ok(productService.deleteCategoryFromProduct(categoryId, productId));
    }

    @DeleteMapping("/{productId}/categories")
    public ResponseEntity<ProductResponse> removeAllCategoriesFromProduct(
            @PathVariable int productId) {
        return ResponseEntity.ok(productService.deleteAllCategoryFromProduct(productId));
    }

    @GetMapping("/{productId}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategoriesFromProduct(
            @PathVariable int productId) {
        return ResponseEntity.ok(productService.findCategoriesByProductId(productId).stream().toList());
    }



}
