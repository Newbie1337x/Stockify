package org.stockify.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.BulkItemResponse;
import org.stockify.model.dto.response.BulkProductResponse;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.mapper.ProductMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CategoryMapper categoryMapper;

    public ProductService(ProductMapper productMapper, ProductRepository productRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public ProductResponse findById(int id) {
        return productMapper.toResponse(getProductById(id));
    }

    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<ProductEntity> page = productRepository.findAll(pageable);

        if (page.isEmpty()) {
            logger.warn("Products list is empty for pageable: {}", pageable);
        }

        return page.map(productMapper::toResponse);
    }

    public ProductResponse save(ProductRequest request) {
        validateUniqueName(request.getName());
        ProductEntity product = productMapper.toEntity(request);
        product.setCategories(getCategoriesFromNames(request.getCategories()));
        return productMapper.toResponse(productRepository.save(product));
    }

    public BulkProductResponse saveAll(List<ProductRequest> requests) {
        List<BulkItemResponse> results = new ArrayList<>();
        int created = 0, skipped = 0, error = 0;

        for (ProductRequest req : requests) {
            try {
                save(req);
                created++;
                results.add(new BulkItemResponse(req.getName(), "CREATED", null));
            } catch (DuplicatedUniqueConstraintException ex) {
                skipped++;
                results.add(new BulkItemResponse(
                        req.getName(),
                        "SKIPPED",
                        "Duplicado, se saltó este ítem"
                ));
            } catch (Exception ex) {
                error++;
                results.add(new BulkItemResponse(
                        req.getName(),
                        "ERROR",
                        ex.getMessage()
                ));
            }
        }
        return new BulkProductResponse(requests.size(),created,skipped,error,results);
    }

    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    public ProductResponse update(int id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        validateUniqueNameForUpdate(request.getName(), id);
        updateProductFields(product, request);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse patch(int id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        if (request.getName() != null) {
            validateUniqueNameForUpdate(request.getName(), id);
        }
        patchProductFields(product, request);
        return productMapper.toResponse(productRepository.save(product));
    }

    public List<ProductResponse> filterByCategories(Set<Integer> categories) {

        return productMapper.toResponseList(
                productRepository.findDistinctByCategoriesId(categories, categories.size())
        );
    }

    public ProductResponse deleteCategoryFromProduct(int categoryId, int productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().remove(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse deleteAllCategoryFromProduct(int productId) {
        ProductEntity product = getProductById(productId);
        product.getCategories().clear();
        return productMapper.toResponse(productRepository.save(product));
    }

    public Set<CategoryResponse> findCategoriesByProductId(int productId){

        if(productRepository.findById(productId).isEmpty()){
            throw new NotFoundException("Product with ID " + productId + " not found");
        }

        if(categoryMapper.toResponseSet(productRepository.findCategoriesByProductId(productId)).isEmpty()){
            throw new NotFoundException("Categories from " + productId + " not found");
        }

        return categoryMapper.toResponseSet(productRepository.findCategoriesByProductId(productId));

    }

    private ProductEntity getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    private CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    private void validateUniqueName(String name) {
        if (productRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicatedUniqueConstraintException("Product with name " + name + " already exists");
        }
    }

    private void validateUniqueNameForUpdate(String name, int id) {
        if (productRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicatedUniqueConstraintException("Product with name " + name + " already exists");
        }
    }

    private Set<CategoryEntity> getCategoriesFromNames(Set<String> categoryNames) {
        return categoryNames.stream()
                .map(name -> categoryRepository.findByName(name)
                        .orElseGet(() -> createCategory(name)))
                .collect(Collectors.toSet());
    }

    private CategoryEntity createCategory(String name) {
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private void updateProductFields(ProductEntity product, ProductRequest request) {
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStock(request.getStock());
    }

    private void patchProductFields(ProductEntity product, ProductRequest request) {
        if (request.getName() != null) product.setName(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getStock() != null) product.setStock(request.getStock());
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            product.getCategories().addAll(getCategoriesFromNames(request.getCategories()));
        }
    }

}