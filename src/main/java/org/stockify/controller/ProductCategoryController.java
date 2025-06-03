package org.stockify.controller;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.assembler.CategoryModelAssembler;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.service.ProductService;


@RestController
@RequestMapping("/api/products/{productId}/categories")
public class ProductCategoryController {

    private final ProductService productService;
    private final CategoryModelAssembler categoryModelAssembler;
    private final ProductModelAssembler productModelAssembler;

    public ProductCategoryController(ProductService productService, CategoryModelAssembler categoryModelAssembler, ProductModelAssembler productModelAssembler) {
        this.productService = productService;
        this.categoryModelAssembler = categoryModelAssembler;
        this.productModelAssembler = productModelAssembler;
    }

    @Operation(summary = "Remove a category from a product")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<EntityModel<ProductResponse>> removeCategoryFromProduct(
            @PathVariable Long productId,
            @PathVariable int categoryId) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.deleteCategoryFromProduct(categoryId, productId)));
    }

    @Operation(summary = "Add a category to a product")
    @PutMapping("/{categoryId}")
    public ResponseEntity<EntityModel<ProductResponse>> addCategoryToProduct(
            @PathVariable Long productId,
            @PathVariable int categoryId
    ){
        return ResponseEntity.ok(productModelAssembler.toModel(productService.addCategoryToProduct(categoryId, productId)));
    }

    @Operation(summary = "Remove all categories from all products")
    @DeleteMapping
    public ResponseEntity<EntityModel<ProductResponse>> removeAllCategoriesFromProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.deleteAllCategoryFromProduct(productId)));
    }

    @Operation(summary = "Get all categories from a specific product")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoryResponse>>> getCategoriesFromProduct(
            @PathVariable Long productId,
            @PageableDefault(sort = "name") Pageable pageable,
            PagedResourcesAssembler<CategoryResponse> assembler) {
        Page<CategoryResponse> categoryPage = productService.findCategoriesByProductId(productId, pageable);
        return ResponseEntity.ok(assembler.toModel(categoryPage, categoryModelAssembler));
    }
}
