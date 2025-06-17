package org.stockify.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.assembler.CategoryModelAssembler;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.service.ProductService;

@RestController
@RequestMapping("/products/{productId}/categories")
@RequiredArgsConstructor
@Tag(name = "ProductCategory", description = "Operations related to product-category relationships")
@SecurityRequirement(name = "bearerAuth")
public class ProductCategoryController {

    private final ProductService productService;
    private final CategoryModelAssembler categoryModelAssembler;
    private final ProductModelAssembler productModelAssembler;

    @Operation(summary = "Remove a category from a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category removed from product successfully"),
            @ApiResponse(responseCode = "404", description = "Product or Category not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<EntityModel<ProductResponse>> removeCategoryFromProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productId,
            @Parameter(description = "ID of the category") @PathVariable int categoryId) {

        return ResponseEntity.ok(
                productModelAssembler.toModel(
                        productService.deleteCategoryFromProduct(categoryId, productId)
                )
        );
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    @Operation(summary = "Add a category to a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category added to product successfully"),
            @ApiResponse(responseCode = "404", description = "Product or Category not found")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<EntityModel<ProductResponse>> addCategoryToProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productId,
            @Parameter(description = "ID of the category") @PathVariable int categoryId) {

        return ResponseEntity.ok(
                productModelAssembler.toModel(
                        productService.addCategoryToProduct(categoryId, productId)
                )
        );
    }

    @Operation(summary = "Remove all categories from a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All categories removed from product successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    @DeleteMapping
    public ResponseEntity<EntityModel<ProductResponse>> removeAllCategoriesFromProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productId) {

        return ResponseEntity.ok(
                productModelAssembler.toModel(
                        productService.deleteAllCategoryFromProduct(productId)
                )
        );
    }

    @Operation(summary = "Get all categories from a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of categories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<CategoryResponse>>> getCategoriesFromProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productId,
            @Parameter(hidden = true) @PageableDefault(sort = "name") Pageable pageable,
            PagedResourcesAssembler<CategoryResponse> assembler) {

        Page<CategoryResponse> categoryPage = productService.findCategoriesByProductId(productId, pageable);
        return ResponseEntity.ok(assembler.toModel(categoryPage, categoryModelAssembler));
    }
}
