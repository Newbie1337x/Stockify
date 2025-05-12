package org.stockify.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.BulkItemResponse;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
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


    public ProductResponse save(ProductRequest request) throws DuplicatedUniqueConstraintException {
            return productMapper.toResponse(productRepository.save(productMapper.toEntity(request)));
        }


    public BulkProductResponse saveAll(List<ProductRequest> requests) {
        List<BulkItemResponse> results = new ArrayList<>();
        int created = 0, skipped = 0, error = 0;

        for (ProductRequest req : requests) {
            try {
                save(req);
                created++;
                results.add(new BulkItemResponse(req.getName(), "CREATED", null));
            } catch (DataIntegrityViolationException ex) {
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
        productMapper.updateEntityFromRequest(request, product);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse patch(int id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.patchEntityFromRequest(request,product);
        return productMapper.toResponse(productRepository.save(product));
    }

    public Page<ProductResponse> filterByCategories(Set<String> categories, Pageable pageable) {
        Page<ProductEntity> page = productRepository.findByAllCategoryNames(categories, categories.size(), pageable);
        return page.map(productMapper::toResponse);
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

}