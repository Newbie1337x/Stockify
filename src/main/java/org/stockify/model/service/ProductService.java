package org.stockify.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.BulkItemResponse;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.mapper.ProductMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.specification.ProductSpecifications;
import org.stockify.model.specification.SpecificationBuilder;
import java.util.ArrayList;
import java.util.List;



@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CategoryMapper categoryMapper;
    private final ProviderRepository providerRepository;

    public ProductService(ProductMapper productMapper, ProductRepository productRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProviderRepository providerRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.providerRepository = providerRepository;
    }

    public ProductResponse findById(Long id) {
        return productMapper.toResponse(getProductById(id));
    }

    public Page<ProductResponse> findAll(Pageable pageable, ProductFilterRequest filterRequest) {
        Specification<ProductEntity> spec = new SpecificationBuilder<ProductEntity>()
                .add(filterRequest.getPrice() != null ? ProductSpecifications.byPrice(filterRequest.getPrice()) : null)
                .add(filterRequest.getName() != null && !filterRequest.getName().isEmpty() ? ProductSpecifications.byName(filterRequest.getName()) : null)
                .add(filterRequest.getDescription() != null && !filterRequest.getDescription().isEmpty() ? ProductSpecifications.byDescription(filterRequest.getDescription()) : null)
                .add(filterRequest.getBarcode() != null && !filterRequest.getBarcode().isEmpty() ? ProductSpecifications.byBarCode(filterRequest.getBarcode()) : null)
                .add(filterRequest.getSku() != null && !filterRequest.getSku().isEmpty() ? ProductSpecifications.bySku(filterRequest.getSku()) : null)
                .add(filterRequest.getBrand() != null && !filterRequest.getBrand().isEmpty() ? ProductSpecifications.byBrand(filterRequest.getBrand()) : null)
                .add(filterRequest.getCategory() != null && !filterRequest.getCategory().isEmpty() ? ProductSpecifications.byCategory(filterRequest.getCategory()) : null)
                .add(filterRequest.getProvider() != null && !filterRequest.getProvider().isEmpty() ? ProductSpecifications.byProvider(filterRequest.getProvider()) : null)
                .add(filterRequest.getProviders() != null && !filterRequest.getProviders().isEmpty() ? ProductSpecifications.byProviders(filterRequest.getProviders()) : null)
                .add(filterRequest.getCategories() != null && !filterRequest.getCategories().isEmpty() ? ProductSpecifications.byCategories(filterRequest.getCategories()) : null)
                .add(filterRequest.getPriceGreater() != null ? ProductSpecifications.byPriceGreaterThan(filterRequest.getPriceGreater()) : null)
                .add(filterRequest.getPriceLess() != null ? ProductSpecifications.byPriceLessThan(filterRequest.getPriceLess()) : null)
                .add(filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2
                        ? ProductSpecifications.byPriceBetween(
                        filterRequest.getPriceBetween().get(0),
                        filterRequest.getPriceBetween().get(1))
                        : null)
                .add(filterRequest.getStock() != null ? ProductSpecifications.byStock(filterRequest.getStock()) : null)
                .add(filterRequest.getStockLessThan() != null ? ProductSpecifications.byStockLessThan(filterRequest.getStockLessThan()) : null)
                .add(filterRequest.getStockGreaterThan() != null ? ProductSpecifications.byStockGreaterThan(filterRequest.getStockGreaterThan()) : null)
                .add(filterRequest.getStockBetween() != null && filterRequest.getStockBetween().size() == 2
                        ? ProductSpecifications.byStockBetween(
                        filterRequest.getStockBetween().get(0),
                        filterRequest.getStockBetween().get(1))
                        : null)
                .build();

        Page<ProductEntity> page = productRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            logger.warn("Products list is empty for pageable: {}", pageable);
        }

        return page.map(productMapper::toResponse);
    }



    public ProductResponse save(ProductRequest request) throws DuplicatedUniqueConstraintException {
        ProductEntity product = productMapper.toEntity(request);

        for (String categoryName : request.categories()) {
            CategoryEntity category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        CategoryEntity newCategory = new CategoryEntity();
                        newCategory.setName(categoryName);
                        return categoryRepository.save(newCategory);
                    });

            product.getCategories().add(category);
        }

        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public BulkProductResponse saveAll(List<ProductRequest> requests) {
        List<BulkItemResponse> results = new ArrayList<>();
        int created = 0, skipped = 0, error = 0;

        for (ProductRequest req : requests) {
            try {
                save(req);
                created++;
                results.add(new BulkItemResponse(req.name(), "CREATED", null));
            } catch (DataIntegrityViolationException ex) {
                skipped++;
                results.add(new BulkItemResponse(
                        req.name(),
                        "SKIPPED",
                        "Duplicado, se saltó este ítem"
                ));
            } catch (DuplicatedUniqueConstraintException ex) {
                skipped++;
                results.add(new BulkItemResponse(
                        req.name(),
                        "SKIPPED",
                        ex.getMessage()
                ));
            } catch (IllegalArgumentException | IllegalStateException ex) {
                error++;
                results.add(new BulkItemResponse(
                        req.name(),
                        "ERROR",
                        "Invalid data: " + ex.getMessage()
                ));
            } catch (Exception ex) {
                error++;
                logger.error("Unexpected error saving product {}: {}", req.name(), ex.getMessage(), ex);
                results.add(new BulkItemResponse(
                        req.name(),
                        "ERROR",
                        "Unexpected error: " + ex.getMessage()
                ));
            }
        }
        return new BulkProductResponse(requests.size(),created,skipped,error,results);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.updateEntityFromRequest(request, product);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse patch(Long id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.patchEntityFromRequest(request,product);
        return productMapper.toResponse(productRepository.save(product));
    }


    //Categories Logic

    public ProductResponse deleteCategoryFromProduct(int categoryId, Long productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().remove(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse deleteAllCategoryFromProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        product.getCategories().clear();
        return productMapper.toResponse(productRepository.save(product));
    }

    public Page<CategoryResponse> findCategoriesByProductId(Long productId, Pageable pageable) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new NotFoundException("Product with ID " + productId + " not found");
        }

        Page<CategoryEntity> categoryPage = productRepository.findCategoriesByProductId(productId, pageable);

        if (categoryPage.isEmpty()) {
            logger.info("No categories found for product ID: {}", productId);
        }

        return categoryPage.map(categoryMapper::toResponse);
    }

    public ProductResponse addCategoryToProduct(int categoryId, Long productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().add(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    //Provider logic

    public ProductResponse assignProviderToProduct(Long productID, Long providerID){
        ProductEntity product = getProductById(productID);
        ProviderEntity provider = getProviderById(providerID);

        product.getProviders().add(provider);
        provider.getProductList().add(product);

        productRepository.save(product);
        providerRepository.save(provider);

        return productMapper.toResponse(product);
    }

    public ProductResponse unassignProviderFromProduct(Long productID, Long providerID){
        ProductEntity product = getProductById(productID);
        ProviderEntity provider = getProviderById(providerID);

        product.getProviders().remove(provider);
        provider.getProductList().remove(product);

        productRepository.save(product);
        providerRepository.save(provider);

        return productMapper.toResponse(product);
    }

    public Page<ProductResponse> findProductsByProviderId(Long providerID, Pageable pageable) {

        Page<ProductResponse> page = productRepository
                .findAllByProviders_Id(providerID, pageable)
                .map(productMapper::toResponse);

        if (page.isEmpty()) {
            logger.warn("No products found for provider ID: {}", providerID);
        }

        return page;
    }


    //Auxiliar
    private ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    private CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    private ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider with ID " + id + " not found"));
    }

}
