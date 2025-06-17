package org.stockify.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.config.GlobalPreferencesConfig;
import org.stockify.dto.request.product.ProductFilterRequest;
import org.stockify.dto.request.stock.StockRequest;
import org.stockify.dto.request.stock.StockTransferRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StockEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.exception.InsufficientStockException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProductStoreMapper;
import org.stockify.model.mapper.StockMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StockRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.specification.StockSpecifications;

import java.util.List;

/**
 * Service for managing stock-related operations in the system.
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductStoreMapper productStoreMapper;
    private final StockMapper stockMapper;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final GlobalPreferencesConfig globalPreferencesConfig;
    private final EmailService emailService;

    /**
     * Lists products available in a specific store, applying filters and pagination.
     *
     * @param storeID        the store ID
     * @param pageable       pagination information
     * @param filterRequest  product filtering criteria
     * @return a page of products with their corresponding stock in the specified store
     */
    public Page<ProductStoreResponse> listProductsByStore(Long storeID, Pageable pageable, ProductFilterRequest filterRequest) {
        Specification<StockEntity> spec = Specification.where(StockSpecifications.byStoreId(storeID))
                .and(StockSpecifications.byProductBarCode(filterRequest.getBarcode()))
                .and(StockSpecifications.byProductSku(filterRequest.getSku()))
                .and(StockSpecifications.byProductName(filterRequest.getName()))
                .and(StockSpecifications.byProductBrand(filterRequest.getBrand()))
                .and(StockSpecifications.byProductDescription(filterRequest.getDescription()))
                .and(StockSpecifications.byProductPrice(filterRequest.getPrice()))
                .and(StockSpecifications.byProductPriceGreaterThan(filterRequest.getPriceGreater()))
                .and(StockSpecifications.byProductPriceLessThan(filterRequest.getPriceLess()))
                .and(StockSpecifications.byProductPriceBetween(
                        filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2
                                ? filterRequest.getPriceBetween().get(0) : null,
                        filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2
                                ? filterRequest.getPriceBetween().get(1) : null))
                .and(StockSpecifications.byProductCategory(filterRequest.getCategory()))
                .and(StockSpecifications.byProductCategories(filterRequest.getCategories()))
                .and(StockSpecifications.byProductProvider(filterRequest.getProvider()))
                .and(StockSpecifications.byProductProviders(filterRequest.getProviders()))
                .and(StockSpecifications.byStockQuantity(filterRequest.getStock()))
                .and(StockSpecifications.byStockQuantityLessThan(filterRequest.getStockLessThan()))
                .and(StockSpecifications.byStockQuantityGreaterThan(filterRequest.getStockGreaterThan()))
                .and(StockSpecifications.byStockQuantityBetween(
                        filterRequest.getStockBetween() != null && filterRequest.getStockBetween().size() == 2
                                ? filterRequest.getStockBetween().get(0) : null,
                        filterRequest.getStockBetween() != null && filterRequest.getStockBetween().size() == 2
                                ? filterRequest.getStockBetween().get(1) : null));

        Page<StockEntity> stock = stockRepository.findAll(spec, pageable);
        return stock.map(s -> productStoreMapper.toResponse(s.getProduct(), s.getQuantity()));
    }

    /**
     * Retrieves a product's stock in a specific store.
     *
     * @param storeID    the store ID
     * @param productID  the product ID
     * @return product-store response
     * @throws NotFoundException if no stock is found
     */
    public ProductStoreResponse getProductByStoreAndProductID(Long storeID, Long productID) {
        StockEntity stock = stockRepository.findByProductIdAndStoreId(productID, storeID)
                .orElseThrow(() -> new NotFoundException("Stock not found for product id: " + productID + " and store id: " + storeID));
        return productStoreMapper.toResponse(stock.getProduct(), stock.getQuantity());
    }

    /**
     * Deletes a product's stock in a specific store.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     */
    public void removeStock(Long productId, Long storeId) {
        stockRepository.deleteByProductIdAndStoreId(productId, storeId);
    }

    /**
     * Retrieves the stock information of a product in a store.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @return stock response DTO
     * @throws NotFoundException if a product, store or stock isn't found
     */
    public StockResponse getStock(Long productId, Long storeId) {
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        return stockMapper.toResponse(stock);
    }

    /**
     * Adds new stock for a product in a store.
     *
     * @param productID the product ID
     * @param storeID   the store ID
     * @param request   stock request with quantity
     * @return the created stock response
     */
    public StockResponse addStock(Long productID, Long storeID, StockRequest request) {
        ProductEntity product = findProduct(productID);
        StoreEntity store = findStore(storeID);
        StockEntity stock = stockMapper.toEntity(request, product, store);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Updates an existing product's stock in a store.
     *
     * @param productID the product ID
     * @param storeID   the store ID
     * @param request   stock request with new quantity
     * @return the updated stock response
     */
    public StockResponse updateStock(Long productID, Long storeID, StockRequest request) {
        StockEntity stock = findStockByProductAndStore(productID, storeID);
        stock.setQuantity(request.quantity());
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Transfers stock from one store to another.
     *
     * @param originStoreID   the ID of the origin store
     * @param transferRequest the request containing destination store and quantity
     * @return a list with the updated stock in both origin and destination stores
     * @throws NotFoundException if any store or product is not found
     * @throws InsufficientStockException if insufficient stock or same origin/destination
     */
    @Transactional
    public List<StockResponse> transferStock(Long originStoreID, StockTransferRequest transferRequest) {
        StockEntity stockFrom = findStockByProductAndStore(transferRequest.productId(), originStoreID);
        StockEntity stockTo = findStockByProductAndStore(transferRequest.productId(), transferRequest.destinationStoreId());

        Double quantityToTransfer = transferRequest.quantity();

        if (stockFrom.getQuantity() < quantityToTransfer) {
            throw new InsufficientStockException("Not enough stock in origin store to transfer.");
        }
        if (originStoreID.equals(transferRequest.destinationStoreId())) {
            throw new InsufficientStockException("Origin and destination store cannot be the same.");
        }

        stockFrom.setQuantity(stockFrom.getQuantity() - quantityToTransfer);
        stockTo.setQuantity(stockTo.getQuantity() + quantityToTransfer);

        StockResponse responseFrom = stockMapper.toResponse(stockRepository.save(stockFrom));
        StockResponse responseTo = stockMapper.toResponse(stockRepository.save(stockTo));

        return List.of(responseFrom, responseTo);
    }

    /**
     * Increases the stock quantity of a product in a store.
     * If the quantity surpasses the alert threshold, the low stock flag is reset.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @param quantity  the quantity to increase
     * @return updated stock response
     */
    public StockResponse increaseStock(Long productId, Long storeId, Double quantity) {
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        stock.setQuantity(stock.getQuantity() + quantity);

        if (stock.getQuantity() > globalPreferencesConfig.getStockAlertThreshold()) {
            stock.setLowStockAlertSent(false);
        }

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Decreases the stock quantity of a product in a store.
     * Sends an email alert if the quantity is below a threshold.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @param quantity  the quantity to decrease
     * @return updated stock response
     * @throws InsufficientStockException if the quantity to decrease exceeds available stock
     */
    public StockResponse decreaseStock(Long productId, Long storeId, Double quantity) {
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        if (stock.getQuantity() < quantity) {
            throw new InsufficientStockException("Stock not enough to decrease");
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        emailService.sendStockAlert(stock);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Utility method to find stock entity by product and store.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @return found StockEntity
     * @throws NotFoundException if product, store or stock is not found
     */
    private StockEntity findStockByProductAndStore(Long productId, Long storeId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
        storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));
        return stockRepository.findByProductIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new NotFoundException("Stock not found for product id: " + productId + " and store id: " + storeId));
    }

    /**
     * Utility method to find a product by ID.
     *
     * @param productId the product ID
     * @return found ProductEntity
     */
    private ProductEntity findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    /**
     * Utility method to find a store by ID.
     *
     * @param storeId the store ID
     * @return found StoreEntity
     */
    private StoreEntity findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));
    }
}
