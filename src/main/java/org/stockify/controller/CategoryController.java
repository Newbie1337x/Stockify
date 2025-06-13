package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.CategoryRequest;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.model.assembler.CategoryModelAssembler;
import org.stockify.model.service.CategoryService;

@Validated
@RestController
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryService categoryService;
    private final CategoryModelAssembler categoryModelAssembler;

    public CategoryController(CategoryService categoryService, CategoryModelAssembler categoryModelAssembler) {
        this.categoryService = categoryService;
        this.categoryModelAssembler = categoryModelAssembler;
    }

    @Operation(summary = "List all categories")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoryResponse>>> listCategories(
            Pageable pageable,
            PagedResourcesAssembler<CategoryResponse> assembler
    ) {
        Page<CategoryResponse> page = categoryService.findAll(pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        PagedModel<EntityModel<CategoryResponse>> pagedModel = assembler.toModel(page, categoryModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get a category by id")
    @GetMapping("/{categoryID}")
    public ResponseEntity<EntityModel<CategoryResponse>> getCategoryById(@PathVariable int categoryID) {
        return ResponseEntity.ok(categoryModelAssembler.toModel(categoryService.findById(categoryID)));
    }

    @Operation(summary = "Delete a category by id")
    @DeleteMapping("/{categoryID}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryID) {
        categoryService.deleteById(categoryID);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<EntityModel<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequestDTO) {

        return ResponseEntity.status(201).body(categoryModelAssembler.toModel(categoryService.save(categoryRequestDTO)));
    }

    @Operation(summary = "Update category name by id")
    @PutMapping("/{categoryID}")
    public ResponseEntity<EntityModel<CategoryResponse>> updateCategory(@PathVariable int categoryID, @Valid @RequestBody CategoryRequest categoryRequestDTO) {
        return ResponseEntity.ok(categoryModelAssembler
                .toModel(categoryService.update(categoryID, categoryRequestDTO)));
    }

    @Operation(summary = "Patch category name by id")
    @PatchMapping("/{categoryID}")
    public ResponseEntity<EntityModel<CategoryResponse>> patchCategory(@PathVariable int categoryID,@Valid @RequestBody CategoryRequest categoryRequestDTO) {
        return ResponseEntity.ok().body(categoryModelAssembler
                .toModel(categoryService.patch(categoryID, categoryRequestDTO)));
    }
}
