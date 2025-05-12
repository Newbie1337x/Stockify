package org.stockify.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.CategoryRequest;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.model.service.CategoryService;

@Validated
@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> listCategories(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<CategoryResponse> page = categoryService.findAll(pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
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
        return ResponseEntity.ok(categoryService.update(id, categoryRequestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> patchCategory(@PathVariable int id,@Valid @RequestBody CategoryRequest categoryRequestDTO) {
        categoryService.patch(id, categoryRequestDTO);
        return ResponseEntity.ok().build();
    }








}
