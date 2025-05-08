package org.stockify.model.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.dto.request.CategoryRequest;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.service.CategoryService;


import java.util.List;
@Validated
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class.getName());

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            logger.warn("category list is empty");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable int id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequestDTO) {

        return ResponseEntity.status(201).body(categoryService.save(categoryRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequest categoryRequestDTO) {
        categoryService.update(id, categoryRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> patchCategory(@PathVariable int id,@Valid @RequestBody CategoryRequest categoryRequestDTO) {
        categoryService.patch(id, categoryRequestDTO);
        return ResponseEntity.ok().build();
    }








}
