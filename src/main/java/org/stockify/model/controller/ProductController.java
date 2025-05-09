package org.stockify.model.controller;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.service.ProductService;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        if (productService.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequest product){

        return ResponseEntity.ok().body(productService.update(id, product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(@PathVariable int id,@RequestBody ProductRequest product){

        return ResponseEntity.ok().body(productService.patch(id,product));
    }

    //Categories logic
    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> filterByCategories(@RequestParam Set<Integer> categoriesIds) {
        return ResponseEntity.ok().body(productService.filterByCategories(categoriesIds));
    }

    @DeleteMapping("/{idProduct}/categories/{idcat}")
    public ResponseEntity<ProductResponse> removeCategoryFromProduct(
            @PathVariable("idProduct") int productId,
            @PathVariable("idcat")    int categoryId
    ) {

        return ResponseEntity.ok(productService.deleteCategoryFromProduct(categoryId, productId));
    }

    @DeleteMapping("/{idProduct}/categories")
    public ResponseEntity<ProductResponse> removeAllCategoryFromProduct(
            @PathVariable("idProduct") int productId
    ) {
       return ResponseEntity.ok(productService.deleteAllCategoryFromProduct(productId));
    }

    @GetMapping("/{idProduct}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategoriesFromProduct(@PathVariable("idProduct") int productId) {
       return ResponseEntity.ok(productService.findCategoriesByProductId(productId).stream().toList()) ;
    }


}
