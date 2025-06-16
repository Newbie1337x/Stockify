package org.stockify.model.service;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.stockify.dto.request.product.ProductCSVRequest;
import org.stockify.dto.request.product.ProductFilterRequest;
import org.stockify.dto.request.product.ProductRequest;
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

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing products in the system.
 * <p>
 * This class handles CRUD operations, CSV imports, category and provider assignments,
 * and other business logic related to products.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CategoryMapper categoryMapper;
    private final ProviderRepository providerRepository;

    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product to find
     * @return a DTO containing the found product's data
     * @throws NotFoundException if no product is found with the given ID
     */
    public ProductResponse findById(Long id) {
        return productMapper.toResponse(getProductById(id));
    }

    /**
     * Saves a new product in the system.
     * Automatically creates any categories specified in the request that do not yet exist.
     *
     * @param request DTO containing the data for the product to create
     * @return a DTO with the created product's data
     * @throws DuplicatedUniqueConstraintException if a product with the same barcode or SKU already exists
     */
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

    /**
     * Saves multiple products in a single batch operation.
     *
     * @param requests a list of DTOs representing the products to create
     * @return a response object summarizing the operation, including counts of created, skipped, and errored items,
     *         as well as detailed results per item
     */
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
                        "Duplicated, item skipped"
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
        return new BulkProductResponse(requests.size(), created, skipped, error, results);
    }

    /**
     * Imports products from a CSV file.
     *
     * @param file the CSV file containing product data
     * @return a response summarizing the import operation including counts and detailed results
     * @throws Exception if an error occurs while processing the file
     */
    public BulkProductResponse importProductsCsv(MultipartFile file) throws Exception {
        InputStreamReader reader = new InputStreamReader(file.getInputStream());

        List<ProductCSVRequest> csvDtos = new CsvToBeanBuilder<ProductCSVRequest>(reader)
                .withType(ProductCSVRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

        List<ProductRequest> requests = csvDtos.stream()
                .map(productMapper::toRequest)
                .collect(Collectors.toList());

        return saveAll(requests);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Fully updates an existing product.
     *
     * @param id      the ID of the product to update
     * @param request DTO containing the new product data
     * @return a DTO with the updated product data
     * @throws NotFoundException if no product is found with the given ID
     */
    public ProductResponse update(Long id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.updateEntityFromRequest(request, product);
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Partially updates an existing product.
     *
     * @param id      the ID of the product to patch
     * @param request DTO containing the product data to update partially
     * @return a DTO with the updated product data
     * @throws NotFoundException if no product is found with the given ID
     */
    public ProductResponse patch(Long id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.patchEntityFromRequest(request, product);
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Removes a category from a product.
     *
     * @param categoryId the ID of the category to remove from the product
     * @param productId  the ID of the product from which to remove the category
     * @return a DTO with the updated product data
     * @throws NotFoundException if the product or category with the specified IDs is not found
     */
    public ProductResponse deleteCategoryFromProduct(int categoryId, Long productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().remove(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Removes all categories from a product.
     *
     * @param productId the ID of the product from which all categories will be removed
     * @return a DTO with the updated product data
     * @throws NotFoundException if the product with the specified ID is not found
     */
    public ProductResponse deleteAllCategoryFromProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        product.getCategories().clear();
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Finds categories associated with a product using pagination.
     *
     * @param productId the ID of the product whose categories are to be retrieved
     * @param pageable  pagination information
     * @return a paginated list of categories associated with the product
     * @throws NotFoundException if the product with the specified ID is not found
     */
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

    /**
     * Adds a category to a product.
     *
     * @param categoryId the ID of the category to add
     * @param productId  the ID of the product to which the category will be added
     * @return a DTO with the updated product data
     * @throws NotFoundException if the product or category with the specified IDs is not found
     */
    public ProductResponse addCategoryToProduct(int categoryId, Long productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().add(category);
        return productMapper.toResponse(productRepository.save(product));
    }


    /**
     * Assigns a provider to a product.
     *
     * @param productID  the ID of the product to assign the provider to
     * @param providerID the ID of the provider to assign
     * @return a DTO with the updated product data
     * @throws NotFoundException         if the product or provider with the specified IDs is not found
     * @throws ResponseStatusException if the provider is already assigned to the product
     */
    public ProductResponse assignProviderToProduct(Long productID, Long providerID) {
        ProductEntity product = getProductById(productID);
        ProviderEntity provider = getProviderById(providerID);
        product.getProviders().add(provider);
        provider.getProductList().add(product);

        try {
            productRepository.save(product);
            providerRepository.save(provider);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Error saving product {} and provider {}: {}", productID, providerID, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Provider " + providerID + " already assigned to product " + productID + " or the other way around");
        }

        return productMapper.toResponse(product);
    }

    /**
     * Unassigns a provider from a product.
     *
     * @param productID  the ID of the product to unassign the provider from
     * @param providerID the ID of the provider to unassign
     * @return a DTO with the updated product data
     * @throws NotFoundException if the product or provider with the specified IDs is not found
     */
    public ProductResponse unassignProviderFromProduct(Long productID, Long providerID) {
        ProductEntity product = getProductById(productID);
        ProviderEntity provider = getProviderById(providerID);

        product.getProviders().remove(provider);
        provider.getProductList().remove(product);

        productRepository.save(product);
        providerRepository.save(provider);

        return productMapper.toResponse(product);
    }

    /**
     * Finds products associated with a provider using pagination.
     *
     * @param providerID the ID of the provider whose products are to be retrieved
     * @param pageable   pagination information
     * @return a paginated list of products associated with the provider
     */
    public Page<ProductResponse> findProductsByProviderId(Long providerID, Pageable pageable) {
        Page<ProductResponse> page = productRepository
                .findAllByProviders_Id(providerID, pageable)
                .map(productMapper::toResponse);

        if (page.isEmpty()) {
            logger.warn("No products found for provider ID: {}", providerID);
        }

        return page;
    }

    // --- Auxiliary methods ---

    /**
     * Auxiliary method to find a product entity by its ID.
     *
     * @param id the ID of the product to find
     * @return the found product entity
     * @throws NotFoundException if no product is found with the given ID
     */
    private ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    /**
     * Auxiliary method to find a category entity by its ID.
     *
     * @param id the ID of the category to find
     * @return the found category entity
     * @throws NotFoundException if no category is found with the given ID
     */
    private CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    /**
     * Auxiliary method to find a provider entity by its ID.
     *
     * @param id the ID of the provider to find
     * @return the found provider entity
     * @throws NotFoundException if no provider is found with the given ID
     */
    private ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider with ID " + id + " not found"));
    }

    /**
     * Searches for products applying filters and pagination.
     * This method builds a dynamic query specification based on the provided filter criteria,
     * allowing for flexible searching by various product attributes such as price, name, description,
     * barcode, SKU, brand, categories, providers, and stock levels.
     * It supports exact matches,
     * ranges, and collections for categories and providers.
     *
     * @param pageable Pagination information including page number, size, and sorting.
     * @param filterRequest DTO containing the filtering criteria to apply.
     *                      Supported filters include:
     *                      - price, priceGreater, priceLess, priceBetween
     *                      - name, description, barcode, sku, brand
     *                      - category, categories
     *                      - provider, providers
     *                      - stock, stockLessThan, stockGreaterThan, stockBetween
     * @return A paginated list (Page) of products matching the filter criteria,
     *         each mapped to a ProductResponse DTO.
     */
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


}
