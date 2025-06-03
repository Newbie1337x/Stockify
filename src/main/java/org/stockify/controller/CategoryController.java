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
@RequestMapping("/api/categories")
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

    @Operation(summary = "Get a categorie by id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoryResponse>> getCategoryById(@PathVariable int id) {
        return ResponseEntity.ok(categoryModelAssembler.toModel(categoryService.findById(id)));
    }

    @Operation(summary = "Delete a category by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<EntityModel<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequestDTO) {

        return ResponseEntity.status(201).body(categoryModelAssembler.toModel(categoryService.save(categoryRequestDTO)));
    }

    @Operation(summary = "Update category name by id")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CategoryResponse>> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequest categoryRequestDTO) {
        return ResponseEntity.ok(categoryModelAssembler
                .toModel(categoryService.update(id, categoryRequestDTO)));
    }

    @Operation(summary = "Patch category name by id")
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<CategoryResponse>> patchCategory(@PathVariable int id,@Valid @RequestBody CategoryRequest categoryRequestDTO) {
        return ResponseEntity.ok().body(categoryModelAssembler
                .toModel(categoryService.patch(id, categoryRequestDTO)));
    }
}
