package org.stockify.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.stockify.dto.request.product.ProductFilterRequest;
import org.stockify.dto.request.product.ProductRequest;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Operations related to product management")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;


    @Operation(summary = "List all products with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of products returned successfully")
    })
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @ParameterObject ProductFilterRequest filter,
            @Parameter(hidden = true)
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> products = productService.findAll(pageable, filter);
        return ResponseEntity.ok(assembler.toModel(products, productModelAssembler));
    }


    @Operation(summary = "Create multiple products in bulk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "207", description = "Multi-status response with results of each product creation")
    })
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<BulkProductResponse> bulkSaveProducts(
            @RequestBody List<@Valid ProductRequest> products) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(productService.saveAll(products));
    }


    @PostMapping(value = "/import", consumes = "multipart/form-data")
    @Operation(summary = "Import products from CSV file")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    @ApiResponse(responseCode = "200", description = "Successful import")
    public ResponseEntity<BulkProductResponse> importProducts(
            @Parameter(description = "CSV file with products", required = true,
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")))
            @RequestParam("file") MultipartFile archivo) {

        try {
            BulkProductResponse response = productService.importProductsCsv(archivo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "Get a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{productID}")
    public ResponseEntity<EntityModel<ProductResponse>> getProductById(
            @Parameter(description = "ID of the product") @PathVariable Long productID) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.findById(productID)));
    }



    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productModelAssembler.toModel(productService.save(product)));
    }


    @Operation(summary = "Delete a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('DELETE')")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productID) {
        productService.deleteById(productID);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Update an existing product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProductResponse>> updateProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productID,
            @Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.update(productID, product)));
    }


    @Operation(summary = "Patch an existing product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product patched successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{productID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProductResponse>> patchProduct(
            @Parameter(description = "ID of the product") @PathVariable Long productID,
            @Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok().body(productModelAssembler.toModel(productService.patch(productID, product)));
    }

}
