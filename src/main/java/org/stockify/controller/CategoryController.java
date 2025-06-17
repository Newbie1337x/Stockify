package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.category.CategoryRequest;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.model.assembler.CategoryModelAssembler;
import org.stockify.model.service.CategoryService;

@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing product categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryModelAssembler categoryModelAssembler;

    @Operation(summary = "List all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of categories retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No categories found")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<CategoryResponse>>> listCategories(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(description = "Filter by category name") @RequestParam(required = false) String name,
            PagedResourcesAssembler<CategoryResponse> assembler
    ) {
        Page<CategoryResponse> page = categoryService.findAll(pageable, name);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(assembler.toModel(page, categoryModelAssembler));
    }

    @Operation(summary = "Get a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{categoryID}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<EntityModel<CategoryResponse>> getCategoryById(
            @Parameter(description = "ID of the category") @PathVariable int categoryID) {

        return ResponseEntity.ok(categoryModelAssembler.toModel(categoryService.findById(categoryID)));
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequestDTO) {

        return ResponseEntity
                .status(201)
                .body(categoryModelAssembler.toModel(categoryService.save(categoryRequestDTO)));
    }

    @Operation(summary = "Update category name by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{categoryID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<CategoryResponse>> updateCategory(
            @Parameter(description = "ID of the category") @PathVariable int categoryID,
            @Valid @RequestBody CategoryRequest categoryRequestDTO) {

        return ResponseEntity.ok(categoryModelAssembler.toModel(
                categoryService.update(categoryID, categoryRequestDTO)));
    }

    @Operation(summary = "Patch category name by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category patched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PatchMapping("/{categoryID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<CategoryResponse>> patchCategory(
            @Parameter(description = "ID of the category") @PathVariable int categoryID,
            @Valid @RequestBody CategoryRequest categoryRequestDTO) {

        return ResponseEntity.ok(categoryModelAssembler.toModel(
                categoryService.patch(categoryID, categoryRequestDTO)));
    }

    @Operation(summary = "Delete a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{categoryID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category") @PathVariable int categoryID) {

        categoryService.deleteById(categoryID);
        return ResponseEntity.ok().build();
    }
}
